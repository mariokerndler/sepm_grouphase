package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableWebMvc
public class ArtworkEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtworkMapper artworkMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Artist artist;

    private Artwork artwork;

    public Artist getTestArtist1() {
        return artist = new Artist("testArtist", "bob", "test", "test", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, "TestDescription", null, 1.0, null, null, null, null, null);
    }

    public Artist getTestArtist2() {
        return artist = new Artist("testArtist2", "bobby", "tester", "test2@test.com", "testStraße 2", passwordEncoder.encode("tester2")
            , false, UserRole.Artist, null, "TestDescription",null, 2.0, null, null, null, null, null);
    }

    public Artwork getArtwork(Long id, byte[] content) {
        return artwork = new Artwork("artwork1", "okay dog pls", "./data/image0", FileType.PNG, getArtistById(id), null, null, content);
    }

    public Artwork getArtwork1(Long id, byte[] content) {
        return artwork = new Artwork("artwork2", "okay no nature", "./data/image1", FileType.PNG, getArtistById(id), null, null, content);
    }

    public Artist getArtistById(Long id) {
        return artistRepository.getById(id);
    }

    @BeforeEach
    public void beforeEach() {
        artistRepository.deleteAll();
        artworkRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @WithMockUser
    public void isDataBaseEmptyBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/artist")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleMessageDto> simpleArtistDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleMessageDto[].class));

        assertEquals(0, simpleArtistDtos.size());
    }

    @Test
    @Transactional
    @WithMockUser
    public void addArtistAndAddArtworks() throws Exception {
        File file = new File("./data/image0.png");
        byte[] image = Files.readAllBytes(file.toPath());

        File file1 = new File("./data/image1.png");
        byte[] image1 = Files.readAllBytes(file1.toPath());

        ApplicationUser anObject = getTestArtist1();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject );

        mockMvc.perform(post("/artist").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
        //assertEquals(UserRole.Artist, artists.get(0).getUserRole());
        //Long id = artists.get(0).getId();


        ApplicationUser anotherObject = getTestArtist2();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(anotherObject);

        mockMvc.perform(post("/artist").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isOk()).andReturn();

        List<ArtistDto> artists2 = allArtists();
        System.out.println(artists2);
        assertEquals(2, artists2.size());
        //Long id1 = artists.get(0).getId();

        assertThat(artists2.contains("bob"));
        assertThat(artists2.contains("bobby"));
        assertThat(artists2.contains("test2@test.com"));
        assertThat(artists2.contains("testStraße 2"));
        assertThat(artists2.contains("testArtist2"));
        assertThat(artists2.contains("description"));
        assertThat(artists2.contains(2.0));

        Artwork anArtwork = getArtwork(1L, image);
        ArtworkDto aDto = artworkMapper.artworkToArtworkDto(anArtwork);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(aDto);
        System.out.println(aDto);


        mockMvc.perform(post("/artwork").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isOk()).andReturn();

        Artwork anotherArtwork = getArtwork1(2L, image1);
        ArtworkDto anotherDto = artworkMapper.artworkToArtworkDto(anotherArtwork);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow4 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson4 = ow4.writeValueAsString(anotherDto);
        System.out.println(anotherDto);


        mockMvc.perform(post("/artwork").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson4))
            .andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/artwork/2").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        Artwork anotherArtwork1 = getArtwork1(2L, null);
        ArtworkDto anotherDto1 = artworkMapper.artworkToArtworkDto(anotherArtwork1);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow5 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson5 = ow5.writeValueAsString(anotherDto1);

        mockMvc.perform(delete("/artwork").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson5))
            .andExpect(status().isOk()).andReturn();

    }

    public List<ArtistDto> allArtists() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/artist/")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ArtistDto> artistResult = objectMapper.readerFor(ApplicationUserDto.class).<ArtistDto>readValues(body).readAll();
        return artistResult;
    }
}
