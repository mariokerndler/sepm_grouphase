package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
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
    private ArtistMapper artistMapper;

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

    public Artwork getArtwork(Long id, byte[] content) {
        return new Artwork("artwork1", "okaydogpls", null, FileType.PNG, getArtistById(id), null, null, content);
    }

    public Artwork getArtwork2(Long id, byte[] content) {
        return new Artwork("artwork2", "BigPringles", null, FileType.PNG, getArtistById(id), null, null, content);
    }

    public Artist getArtistById(Long id) {
        return artistRepository.getById(id);
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        artworkRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

        ArtistDto anObject = artistMapper.artistToArtistDto(getTestArtist1());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
        Long id = artists.get(0).getId();

        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");

        Artwork anArtwork = getArtwork(id, image);
        ArtworkDto aDto = artworkMapper.artworkToArtworkDto(anArtwork);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(aDto);

        mockMvc.perform(post("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isCreated()).andReturn();
    }

    @Test
    @WithMockUser
    public void hasDataBaseOneArtistAndOneArtworkBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/artists")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
        Long id = artists.get(0).getId();

        assertTrue(artists.toString().contains("bob"));
        assertTrue(artists.toString().contains("testArtist"));
        assertTrue(artists.toString().contains("test"));
        assertTrue(artists.toString().contains("test@test.com"));
        assertTrue(artists.toString().contains("test"));
        assertTrue(artists.toString().contains("1.0"));

        mockMvc.perform(get("/api/v1/artworks/" + id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        List<ArtworkDto> artworks = allArtworksByArtist(id);
        assertEquals(1, artworks.size());
        assertTrue(artworks.toString().contains("okaydogpls"));
        assertTrue(artworks.toString().contains("artwork1"));
    }

    @Test
    @Transactional
    @WithMockUser
    public void addArtwork() throws Exception {
        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/7yHp276/image1.jpg");

        List<ArtistDto> artists = allArtists();
        Long id = artists.get(0).getId();

        Artwork anArtwork = getArtwork2(id, image);
        ArtworkDto aDto = artworkMapper.artworkToArtworkDto(anArtwork);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(aDto);

        mockMvc.perform(post("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtworkDto> artworks = allArtworksByArtist(id);
        assertEquals(2, artworks.size());
        assertTrue(artworks.toString().contains("okaydogpls"));
        assertTrue(artworks.toString().contains("artwork1"));
        assertTrue(artworks.toString().contains("BigPringles"));
        assertTrue(artworks.toString().contains("artwork2"));
    }

    @Test
    @Transactional
    @WithMockUser
    public void getArtworkByOwner() throws Exception {
        List<ArtistDto> artists = allArtists();
        Long id = artists.get(0).getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/artworks/" + id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ArtworkDto> artworks = allArtworksByArtist(id);
        assertEquals(1, artworks.size());

        assertTrue(artworks.toString().contains("okaydogpls"));
        assertTrue(artworks.toString().contains("artwork1"));
    }

    @Test
    @Transactional
    @WithMockUser
    public void deleteArtwork() throws Exception {
        List<ArtistDto> artists = allArtists();
        Long id = artists.get(0).getId();

        List<ArtworkDto> artworkDtos = allArtworksByArtist(id);
        ArtworkDto aDto = artworkDtos.get(0);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(aDto);

        mockMvc.perform(delete("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk()).andReturn();

        List<ArtworkDto> artworks = allArtworksByArtist(id);
        assertEquals(0, artworks.size());
        assertFalse(artworks.toString().contains("okaydogpls"));
        assertFalse(artworks.toString().contains("artwork1"));
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
        assertEquals(2, users.size());
        assertEquals(UserRole.User, userRepository.findApplicationUserByEmail(userDto.getEmail().toLowerCase()).getUserRole());
        Long userId = userRepository.findApplicationUserByEmail(userDto.getEmail().toLowerCase()).getId();


        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");
        ArtworkDto artworkDto = new ArtworkDto("Artwork by User",
            "This is an artwork posted by a user that is not an artist",
            image, null, FileType.PNG, userId, null, null, null);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(artworkDto);

        mockMvc.perform(post("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().is4xxClientError()).andReturn();
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
