package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtworkMapper artworkMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApplicationUser getTestUser() {
        return ApplicationUser.builder()
            .userName("testUser")
            .name("Johnny")
            .surname("Burger")
            .email("jonBB@testmail.com")
            .address("Frystreet 37 L.A.")
            .password(passwordEncoder.encode("onionrings"))
            .admin(false)
            .userRole(UserRole.User)
            .build();
    }

    public Artist getTestArtist1() {
        return Artist.builder()
            .userName("testArtist")
            .name("bob")
            .surname("test")
            .email("test@test.com")
            .address("test")
            .password(passwordEncoder.encode("test"))
            .admin(false)
            .userRole(UserRole.Artist)
            .description("TestDescription")
            .reviewScore(1.0)
            .build();
    }

    public Artist getTestArtist2() {
        return Artist.builder()
            .userName("testArtist2")
            .name("bobby")
            .surname("tester")
            .email("test2@test.com")
            .address("testStraße 2")
            .password(passwordEncoder.encode("tester2"))
            .admin(false)
            .userRole(UserRole.Artist)
            .description("TestDescription")
            .reviewScore(2.0)
            .build();
    }

    public Artwork getArtwork(Long id, byte[] content) {
        return new Artwork("artwork1", "okaydogpls", null, FileType.PNG, getArtistById(id), null, null, content);
    }

    public Artist getArtistById(Long id) {
        return artistRepository.getById(id);
    }

    @BeforeEach
    public void beforeEach() {
        artworkRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @WithMockUser
    public void isDataBaseEmptyBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/artists")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artistDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ArtistDto[].class));

        assertEquals(0, artistDtos.size());
    }

    @Test
    @Transactional
    @WithMockUser
    public void addArtistAndAddArtworks_getArtworkAndFindOwner_ThenDeleteOneArtwork() throws Exception {

        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");

        ApplicationUser anObject = getTestArtist1();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());


        ApplicationUser anotherObject = getTestArtist2();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(anotherObject);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists2 = allArtists();
        assertEquals(2, artists2.size());

        assertTrue(artists2.toString().contains("bob"));
        assertTrue(artists2.toString().contains("bobby"));
        assertTrue(artists2.toString().contains("test2@test.com"));
        assertTrue(artists2.toString().contains("testStraße 2"));
        assertTrue(artists2.toString().contains("testArtist2"));
        assertTrue(artists2.toString().contains("2.0"));

        Long artistIdForArtwork = artists.get(0).getId();

        Artwork anArtwork = getArtwork(artistIdForArtwork, image);
        ArtworkDto aDto = artworkMapper.artworkToArtworkDto(anArtwork);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(aDto);

        mockMvc.perform(post("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isCreated()).andReturn();


        mockMvc.perform(get("/api/v1/artworks/" + artistIdForArtwork).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        List<ArtworkDto> artworks = allArtworksByArtist(artistIdForArtwork);
        assertEquals(1, artworks.size());

        assertTrue(artworks.toString().contains("okaydogpls"));
        assertTrue(artworks.toString().contains("artwork1"));

        List<ArtworkDto> artworkDtos = allArtworksByArtist(artistIdForArtwork);
        ArtworkDto anotherDto1 = artworkDtos.get(0);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow5 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson5 = ow5.writeValueAsString(anotherDto1);

        mockMvc.perform(delete("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson5))
            .andExpect(status().isOk()).andReturn();

        List<ArtworkDto> artworks1 = allArtworksByArtist(artistIdForArtwork);
        assertEquals(0, artworks1.size());

    }


    @Test
    @Transactional
    @WithMockUser
    public void givenNothing_addUser_postArtworkByUser_expectBadRequest() throws Exception {
        ApplicationUserDto userDto = userMapper.userToUserDto(getTestUser());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userDto);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(1, users.size());
        assertEquals(UserRole.User, users.get(0).getUserRole());
        Long userId = userRepository.findApplicationUserByEmail(userDto.getEmail().toLowerCase()).getId();


        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");
        ArtworkDto artworkDto = new ArtworkDto("Artwork by User",
            "This is an artwork posted by a user that is not an artist",
            image, null, FileType.PNG, userId, null, null);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(artworkDto);

        mockMvc.perform(post("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isNotFound()).andReturn();
    }

    public List<ArtistDto> allArtists() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/artists")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ArtistDto> artistResult = objectMapper.readerFor(ArtistDto.class).<ArtistDto>readValues(body).readAll();
        return artistResult;
    }

    public List<ArtworkDto> allArtworksByArtist(Long id) throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/artworks/" + id)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ArtworkDto> artworkResult = objectMapper.readerFor(ArtworkDto.class).<ArtworkDto>readValues(body).readAll();
        return artworkResult;
    }

    public List<ApplicationUserDto> allUsers() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ApplicationUserDto> userResult = objectMapper.readerFor(ApplicationUserDto.class).<ApplicationUserDto>readValues(body).readAll();
        return userResult;
    }

}
