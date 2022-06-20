package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GetImageByteArray;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
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
@AutoConfigureWebMvc
public class ApplicationUserEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApplicationUser getTestUser1() {
        return new ApplicationUser("testUser", null, "bob", "atest",
            "test@atest.com", "testStraße 3", passwordEncoder.encode("atest"), false, UserRole.User);
    }

    public ApplicationUser getTestUser2() {
        return new ApplicationUser("testUser2", null, "bobby", "aSecondTest",
            "test2@atest.com", "testStraße 2", passwordEncoder.encode("SecondTest"), false, UserRole.User);
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        userRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

        ApplicationUser anObject = getTestUser1();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(1, users.size());
    }

    @Test
    @WithMockUser
    public void hasDataBaseOneUserBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ApplicationUserDto> users = allUsers();

        assertEquals(1, users.size());
        assertTrue(users.toString().contains("testUser"));
        assertTrue(users.toString().contains("bob"));
        assertTrue(users.toString().contains("atest"));
        assertTrue(users.toString().contains("test@atest.com"));
        assertTrue(users.toString().contains("testStraße 3"));
    }

    @Test
    @WithMockUser()
    public void addAUser() throws Exception {
        ApplicationUser anotherObject = getTestUser2();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(anotherObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(2, users.size());
        assertTrue(users.toString().contains("testUser"));
        assertTrue(users.toString().contains("bob"));
        assertTrue(users.toString().contains("atest"));
        assertTrue(users.toString().contains("test@atest.com"));
        assertTrue(users.toString().contains("testStraße 3"));
        assertTrue(users.toString().contains("testUser2"));
        assertTrue(users.toString().contains("bobby"));
        assertTrue(users.toString().contains("aSecondTest"));
        assertTrue(users.toString().contains("test2@atest.com"));
        assertTrue(users.toString().contains("testStraße 2"));
    }

    @Test
    @WithMockUser()
    public void modifyUser() throws Exception {
        List<ApplicationUserDto> user = allUsers();
        Long id = user.get(0).getId();

        ApplicationUser modifiedObject = new ApplicationUser(id, String.format("testUser"), null, "bobName", "aSecondTest",
            "testmodify@atest.com", "testModStraße 2", passwordEncoder.encode("SecondTest"), false, UserRole.User);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(modifiedObject);

        mockMvc.perform(put("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isOk()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(1, users.size());
        assertTrue(users.toString().contains("testmodify@atest.com"));
        assertFalse(users.toString().contains("test@atest.com"));
        assertTrue(users.toString().contains("testUser"));
        assertTrue(users.toString().contains("bobName"));
        assertTrue(users.toString().contains("aSecondTest"));
        assertTrue(users.toString().contains("testModStraße 2"));
    }

    @Test
    @WithMockUser()
    public void deleteUser() throws Exception {
        List<ApplicationUserDto> user = allUsers();
        Long id = user.get(0).getId();

        mockMvc.perform(delete("/api/v1/users/" + id))
            .andExpect(status().isOk()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(0, users.size());
        assertFalse(users.toString().contains("testUser"));
        assertFalse(users.toString().contains("bob"));
        assertFalse(users.toString().contains("atest"));
        assertFalse(users.toString().contains("test@atest.com"));
        assertFalse(users.toString().contains("testStraße 3"));
    }





    /*@Test
    @WithMockUser()
    public void addTwoUsersAndModifyOne() throws Exception {
        ApplicationUser anObject = getTestUser1();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(1, users.size());

        ApplicationUser anotherObject = getTestUser2();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(anotherObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users2 = allUsers();
        assertEquals(2, users2.size());

        assertTrue(users2.toString().contains("bob"));
        assertTrue(users2.toString().contains("bobby"));
        assertTrue(users2.toString().contains("test@atest.com"));
        assertTrue(users2.toString().contains("testStraße 2"));

        ApplicationUser modifiedObject = applicationUser = new ApplicationUser(2L, String.format("testUser2"), null, "bobbyName", "aSecondTest",
            "testmodify@atest.com", "testStraße 2", passwordEncoder.encode("SecondTest"), false, UserRole.User);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(modifiedObject);

        mockMvc.perform(put("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isOk()).andReturn();

        List<ApplicationUserDto> users3 = allUsers();
        assertEquals(2, users3.size());
        assertTrue(users3.toString().contains("testmodify@atest.com"));
        assertTrue(!users3.toString().contains("test2@atest.com"));

     */
    @Test
    @WithMockUser
    public void upgradeUserToArtist_upgradesRelevantFields() throws Exception {

        ApplicationUserDto userDto = userMapper.userToUserDto(getTestUser1());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userDto);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        assertEquals(1, allUsers().size());
        assertEquals(0, allArtists().size());

        userDto.setId(allUsers().get(0).getId());

        mockMvc.perform(put("/api/v1/users/" + userDto.getId() + "/upgrade"))
            .andExpect(status().isOk()).andReturn();

        assertEquals(1, allUsers().size());
        assertEquals(1, allArtists().size());

        userDto = allUsers().get(0);
        ArtistDto artistDto = allArtists().get(0);

        assertEquals(userDto.getId(), artistDto.getId());
        assertEquals(userDto.getUserName(), artistDto.getUserName());
        assertEquals(userDto.getName(), artistDto.getName());
        assertEquals(userDto.getSurname(), artistDto.getSurname());
        assertEquals(userDto.getEmail(), artistDto.getEmail());
        assertEquals(userDto.getAddress(), artistDto.getAddress());
        assertEquals(userDto.getPassword(), artistDto.getPassword());
        assertEquals(UserRole.Artist, userDto.getUserRole());
        assertEquals(UserRole.Artist, artistDto.getUserRole());
    }

    @Test
    @WithMockUser
    public void upgradeUserToArtist_makesArtworkUploadPossible() throws Exception {

        ApplicationUserDto userDto = userMapper.userToUserDto(getTestUser1());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userDto);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();


        assertEquals(1, allUsers().size());
        assertEquals(0, allArtists().size());

        userDto.setId(allUsers().get(0).getId());

        mockMvc.perform(put("/api/v1/users/" + userDto.getId() + "/upgrade"))
            .andExpect(status().isOk()).andReturn();

        assertEquals(1, allUsers().size());
        assertEquals(1, allArtists().size());

        byte[] image = GetImageByteArray.getImageBytes("https://i.ibb.co/HTT7Ym3/image0.jpg");
        ArtworkDto artworkDto = new ArtworkDto("Artwork by new Artist",
            "This is the description of an artwork posted by a rookie artist",
            image, null, FileType.PNG, userDto.getId(), null, null, null);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow1 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson1 = ow1.writeValueAsString(artworkDto);

        mockMvc.perform(post("/api/v1/artworks").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1))
            .andExpect(status().isCreated()).andReturn();

        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/artworks")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ArtworkDto> artworkResult = objectMapper.readerFor(ArtworkDto.class).<ArtworkDto>readValues(body).readAll();
        System.out.println(artworkResult.get(0).toString());
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

    public List<ApplicationUserDto> allUsers() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/users/")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ApplicationUserDto> userResult = objectMapper.readerFor(ApplicationUserDto.class).<ApplicationUserDto>readValues(body).readAll();
        return userResult;
    }
}
