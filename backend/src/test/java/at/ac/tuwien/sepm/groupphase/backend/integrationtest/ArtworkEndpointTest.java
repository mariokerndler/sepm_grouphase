package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
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

import static org.junit.jupiter.api.Assertions.*;
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
    private PasswordEncoder passwordEncoder;

    public Artist getTestArtist1() {
        return new Artist("testArtist", "bob", "test", "test@test.com", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, "TestDescription", null, 1.0, null, null, null, null, null);
    }

    public Artist getTestArtist2() {
        return new Artist("testArtist2", "bobby", "tester", "test2@test.com", "testStraße 2", passwordEncoder.encode("tester2")
            , false, UserRole.Artist, null, "TestDescription",null, 2.0, null, null, null, null, null);
    }

    public Artwork getArtwork(Long id, byte[] content) {
        return new Artwork("artwork1", "okaydogpls", null, FileType.PNG, getArtistById(id), null, null, content);
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
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/artists")).andDo(print()).andReturn();
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
    public void addArtistAndAddArtworks_getArtworkAndFindOwnerThenDeleteOneArtwork() throws Exception {

        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");

        ApplicationUser anObject = getTestArtist1();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject );

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());


        ApplicationUser anotherObject = getTestArtist2();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(anotherObject);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isOk()).andReturn();

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
            .andExpect(status().isOk()).andReturn();


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
                .get("/api/v1/artworks/"+id)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ArtworkDto> artworkResult = objectMapper.readerFor(ArtworkDto.class).<ArtworkDto>readValues(body).readAll();
        return artworkResult;
    }
}
