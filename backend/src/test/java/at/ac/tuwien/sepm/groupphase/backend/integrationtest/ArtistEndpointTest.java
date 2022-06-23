package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
public class ArtistEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @BeforeEach
    public void beforeEach() throws Exception {
        artistRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        Artist anObject = getTestArtist1();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
    }

    @Test
    @WithMockUser
    public void hasDataBaseOneArtistBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/artists")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());

        assertTrue(artists.toString().contains("bob"));
        assertTrue(artists.toString().contains("testArtist"));
        assertTrue(artists.toString().contains("test"));
        assertTrue(artists.toString().contains("test@test.com"));
        assertTrue(artists.toString().contains("test"));
        assertTrue(artists.toString().contains("Artist"));
        assertTrue(artists.toString().contains("1.0"));
    }

    @Test
    @WithMockUser()
    public void addOneArtist() throws Exception {
        Artist anObject = getTestArtist2();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(2, artists.size());

        assertTrue(artists.toString().contains("bob"));
        assertTrue(artists.toString().contains("bobby"));
        assertTrue(artists.toString().contains("test2@test.com"));
        assertTrue(artists.toString().contains("testStraße 2"));
        assertTrue(artists.toString().contains("testArtist2"));
        assertTrue(artists.toString().contains("Artist"));
        assertTrue(artists.toString().contains("2.0"));
    }

    @Test
    @WithMockUser()
    public void modifyArtist() throws Exception {
        List<ArtistDto> artist = allArtists();
        Long id = artist.get(0).getId();

        Artist modifiedObject = Artist.builder()
            .id(id)
            .userName("testArtist")
            .name("bobMod")
            .surname("test")
            .email("testMod@test.com")
            .address("testStraße 1")
            .password(passwordEncoder.encode("test"))
            .admin(false)
            .userRole(UserRole.Artist)
            .description("TestModDescription")
            .reviewScore(1.0)
            .build();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(modifiedObject);

        mockMvc.perform(put("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isOk()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());

        assertTrue(artists.toString().contains("bobMod"));
        assertTrue(artists.toString().contains("testArtist"));
        assertTrue(artists.toString().contains("test"));
        assertTrue(artists.toString().contains("testMod@test.com"));
        assertTrue(artists.toString().contains("testStraße 1"));
        assertTrue(artists.toString().contains("Artist"));
        assertTrue(artists.toString().contains("1.0"));

    }

    @Test
    @WithMockUser()
    public void deleteArtist() throws Exception {
        List<ArtistDto> artist = allArtists();
        Long id = artist.get(0).getId();

        mockMvc.perform(delete("/api/v1/artists/" + id))
            .andExpect(status().isOk()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(0, artists.size());

        assertFalse(artists.toString().contains("bob"));
        assertFalse(artists.toString().contains("testArtist"));
        assertFalse(artists.toString().contains("test"));
        assertFalse(artists.toString().contains("test@test.com"));
        assertFalse(artists.toString().contains("test"));
        assertFalse(artists.toString().contains("Artist"));
        assertFalse(artists.toString().contains("1.0"));
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
}
