package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ChatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ChatMessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableWebMvc
public class ChatEndpointTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    ChatMessageMapper chatMessageMapper;
    @Autowired
    ChatMapper chatMapper;
    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ArtistMapper artistMapper;
    @Autowired
    private ObjectMapper objectMapper;


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

    public ApplicationUser getTestUser2() {
        return ApplicationUser.builder()
            .userName("testUser2")
            .name("Johnfny")
            .surname("Bufrger")
            .email("jonBBV@testmail.com")
            .address("Frystreet 37 L.A.")
            .password(passwordEncoder.encode("onionrings"))
            .admin(false)
            .userRole(UserRole.User)
            .build();
    }

    public ChatDto getTestChat() {
        return ChatDto.builder()
            .chatPartnerId(getTestUser2().getId())
            .userId(getTestUser().getId())
            .build();
    }

    @Transactional
    @BeforeEach
    public void beforeEach() throws Exception {
        artworkRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

        ApplicationUserDto anObject = userMapper.userToUserDto(getTestUser());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(1, users.size());
        anObject = userMapper.userToUserDto(getTestUser2());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ow = objectMapper.writer().withDefaultPrettyPrinter();
        requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        users = allUsers();
        assertEquals(2, users.size());
    }


    @Test
    @Transactional
    @WithMockUser
    public void chatCreatedSuccessfully() throws Exception {
        List<ApplicationUserDto> users = allUsers();
        Long id = users.get(0).getId();

        ChatDto chatDto = getTestChat();
        chatDto.setChatPartnerId(users.get(0).getId());
        chatDto.setUserId(users.get(1).getId());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(chatDto);

        mockMvc.perform(post("/api/v1//chats/chat").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUser> chats = allChatsForUser(id);
        assertEquals(1, chats.size());
        assertTrue(chats.get(0).getId() == users.get(1).getId());
        mockMvc.perform(post("/api/v1//chats/chat").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isUnprocessableEntity()).andReturn();
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

    public List<ApplicationUser> allChatsForUser(Long id) throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/chats/" + id)
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ApplicationUser> chatsResult = objectMapper.readerFor(ApplicationUser.class).<ApplicationUser>readValues(body).readAll();
        return chatsResult;
    }
}
