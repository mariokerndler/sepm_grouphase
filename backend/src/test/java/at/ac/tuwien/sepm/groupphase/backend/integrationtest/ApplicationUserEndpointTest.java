package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
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
    private ArtistRepository artistRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationUser applicationUser;

    public ApplicationUser getTestUser1() {
        return applicationUser = new ApplicationUser(String.format("testUser"), null, "bob", "atest",
            "test@atest.com", "testStraße 3", passwordEncoder.encode("atest"), false, UserRole.User);
    }

    public ApplicationUser getTestUser2() {
        return applicationUser = new ApplicationUser(String.format("testUser2"), null, "bobby", "aSecondTest",
            "test2@atest.com", "testStraße 2", passwordEncoder.encode("SecondTest"), false, UserRole.User);
    }

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @WithMockUser
    public void isDataBaseEmptyBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleMessageDto> simpleUserDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleMessageDto[].class));

        assertEquals(0, simpleUserDtos.size());
    }

    @Test
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
        assertFalse(users3.toString().contains("test2@atest.com"));

        Long userId = users3.get(0).getId();

        mockMvc.perform(delete("/api/v1/users/" + userId))
            .andExpect(status().isOk()).andReturn();

        List<ApplicationUserDto> users4 = allUsers();
        assertEquals(1, users4.size());
        assertFalse(users4.toString().contains("test@atest.com"));
    }

    @Test
    @WithMockUser
    public void upgradeUserToArtist_upgradesRelevantFields() throws Exception {

        ApplicationUser user = getTestUser1();
        userRepository.save(user);

        assertEquals(1, userRepository.findAll().size());
        assertEquals(0, artistRepository.findAll().size());

        mockMvc.perform(put("/api/v1/users/" + user.getId() + "/upgrade"))
            .andExpect(status().isOk()).andReturn();

        assertEquals(1, userRepository.findAll().size());
        assertEquals(1, artistRepository.findAll().size());

        Artist artist = artistRepository.findAll().get(0);

        assertEquals(user.getId(), artist.getId());
        assertEquals(user.getUserName(), artist.getUserName());
        assertEquals(user.getName(), artist.getName());
        assertEquals(user.getSurname(), artist.getSurname());
        assertEquals(user.getEmail(), artist.getEmail());
        assertEquals(user.getAddress(), artist.getAddress());
        assertEquals(user.getPassword(), artist.getPassword());
        assertEquals(user.getUserRole(), UserRole.User);
        assertEquals(UserRole.Artist, artist.getUserRole());
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
