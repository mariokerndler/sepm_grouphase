package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
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

import java.time.LocalDateTime;
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
public class CommissionEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtworkMapper artworkMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommissionMapper commissionMapper;

    @BeforeEach
    public void beforeEach() throws Exception {
        commissionRepository.deleteAll();
        artistRepository.deleteAll();
        userRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

        Artist artist = Artist.builder().userName("momo45").name("Millie").surname("Bobbington").email("mil.b@aol.de").address("Mispelstreet").password("passwordhash").admin(false).userRole(UserRole.Artist).description("Description of new artist").profileSettings("settings string").build();

        ApplicationUser user = ApplicationUser.builder().userName("sunscreen98").name("Mike").surname("Regen").email("mikey98@gmail.com").address("Greatstreet").password("passwordhash2").admin(false).userRole(UserRole.User).build();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(artist);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
        Long artistId = artists.get(0).getId();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow1 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson1 = ow1.writeValueAsString(user);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(2, users.size());
        Long userId = users.get(1).getId();

        Artist artist1 = Artist.builder().id(artistId).userName("momo45").name("Millie").surname("Bobbington").email("mil.b@aol.de").address("Mispelstreet").password("passwordhash").admin(false).userRole(UserRole.Artist).description("Description of new artist").profileSettings("settings string").build();

        ApplicationUser user1 = ApplicationUser.builder().id(userId).userName("sunscreen98").name("Mike").surname("Regen").email("mikey98@gmail.com").address("Greatstreet").password("passwordhash2").admin(false).userRole(UserRole.User).build();

        Commission commission = Commission.builder().artist(artist1).customer(user1).sketchesShown(3).feedbackSent(0).price(300).issueDate(LocalDateTime.now()).deadlineDate(LocalDateTime.now().plusDays(20)).title("its yours").instructions("do the thing").status(CommissionStatus.OPEN).feedbackRounds(2).build();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(commissionMapper.commissionToDetailedCommissionDto(commission));

        mockMvc.perform(post("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        List<SimpleCommissionDto> commissions = allCommissions();
        assertEquals(1, commissions.size());
        assertTrue(commissions.toString().contains("do the thing"));
    }

    @Test
    @WithMockUser
    public void hasDataBaseOneArtistBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/commissions")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleCommissionDto> simpleCommissionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleCommissionDto[].class));

        assertEquals(1, simpleCommissionDtos.size());

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());

        assertTrue(artists.toString().contains("momo45"));
        assertTrue(artists.toString().contains("Millie"));
        assertTrue(artists.toString().contains("Bobbington"));
        assertTrue(artists.toString().contains("mil.b@aol.de"));
        assertTrue(artists.toString().contains("Mispelstreet"));
        assertTrue(artists.toString().contains("settings string"));
    }

    @Test
    @WithMockUser
    public void hasDataBaseOneUserBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/commissions")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleCommissionDto> simpleCommissionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleCommissionDto[].class));

        assertEquals(1, simpleCommissionDtos.size());

        List<ApplicationUserDto> users = allUsers();
        assertEquals(2, users.size());

        assertTrue(users.toString().contains("sunscreen98"));
        assertTrue(users.toString().contains("Mike"));
        assertTrue(users.toString().contains("Regen"));
        assertTrue(users.toString().contains("mikey98@gmail.com"));
        assertTrue(users.toString().contains("Greatstreet"));
    }

    @Test
    @WithMockUser
    public void hasDataBaseOneCommissionBeforeTests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/commissions")).andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SimpleCommissionDto> simpleCommissionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SimpleCommissionDto[].class));

        assertEquals(1, simpleCommissionDtos.size());

        List<SimpleCommissionDto> commissions = allCommissions();
        assertEquals(1, commissions.size());
        assertTrue(commissions.toString().contains("do the thing"));
        assertTrue(commissions.toString().contains("its yours"));
    }

    @Test
    @Transactional
    @WithMockUser()
    public void addCommission() throws Exception {
        List<ApplicationUserDto> users = allUsers();
        List<ArtistDto> artists = allArtists();

        Commission commission = Commission.builder()
            .artist(artistMapper.artistDtoToArtist(artists.get(0)))
            .customer(userMapper.userDtoToUser(users.get(0)))
            .sketchesShown(4)
            .feedbackSent(0)
            .price(8000)
            .issueDate(LocalDateTime.now())
            .deadlineDate(LocalDateTime.now().plusWeeks(12))
            .title("I want to be a french woman")
            .instructions("draw me like one of your french girls")
            .status(CommissionStatus.OPEN)
            .build();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(commissionMapper.commissionToDetailedCommissionDto(commission));

        mockMvc.perform(post("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<SimpleCommissionDto> commissions = allCommissions();
        assertEquals(2, commissions.size());
        assertTrue(commissions.toString().contains("I want to be a french woman"));
        assertTrue(commissions.toString().contains("draw me like one of your french girls"));
    }

    @Test
    @Transactional
    @WithMockUser()
    public void modifyCommission() throws Exception {
        List<ApplicationUserDto> users = allUsers();
        List<ArtistDto> artists = allArtists();
        List<SimpleCommissionDto> commissions1 = allCommissions();
        Commission commission = commissionMapper.simpleCommissionDtoToCommission(commissions1.get(0));

        Commission modCommission = Commission.builder().id(commissions1.get(0).getId()).feedbackRounds(2).artist(artistMapper.artistDtoToArtist(artists.get(0))).customer(userMapper.userDtoToUser(users.get(0))).sketchesShown(3).feedbackSent(0).price(300).issueDate(commission.getIssueDate()).deadlineDate(commission.getDeadlineDate()).title("Drawing french").status(CommissionStatus.OPEN).instructions("draw me like one of your french boys").build();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(commissionMapper.commissionToDetailedCommissionDto(modCommission));

        mockMvc.perform(put("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk()).andReturn();

        List<SimpleCommissionDto> commissions = allCommissions();
        assertEquals(1, commissions.size());
        assertFalse(commissions.toString().contains("I want to be a french woman"));
        assertFalse(commissions.toString().contains("draw me like one of your french girls"));
        assertFalse(commissions.toString().contains("its yours"));
        assertFalse(commissions.toString().contains("do the thing"));
        assertTrue(commissions.toString().contains("draw me like one of your french boys"));
        assertTrue(commissions.toString().contains("Drawing french"));
    }

    @Test
    @Transactional
    @WithMockUser()
    public void deleteCommission() throws Exception {
        List<ApplicationUserDto> users = allUsers();
        List<ArtistDto> artists = allArtists();
        List<SimpleCommissionDto> commissions1 = allCommissions();
        Commission commission = commissionMapper.simpleCommissionDtoToCommission(commissions1.get(0));

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(commissionMapper.commissionToDetailedCommissionDto(commission));

        mockMvc.perform(delete("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk()).andReturn();

        List<SimpleCommissionDto> commissions = allCommissions();
        assertEquals(0, commissions.size());
        assertFalse(commissions.toString().contains("I want to be a french woman"));
        assertFalse(commissions.toString().contains("draw me like one of your french girls"));
        assertFalse(commissions.toString().contains("its yours"));
        assertFalse(commissions.toString().contains("do the thing"));
        assertFalse(commissions.toString().contains("draw me like one of your french boys"));
        assertFalse(commissions.toString().contains("Drawing french"));
    }

    /*@Test
    @Transactional
    @WithMockUser
    public void addCommission_modifyAndDelete() throws Exception {
        Artist artist = Artist.builder().userName("momo45").name("Millie").surname("Bobbington").email("mil.b@aol.de").address("Mispelstreet").password("passwordhash").admin(false).userRole(UserRole.Artist).description("Description of new artist").profileSettings("settings string").build();

        ApplicationUser user = ApplicationUser.builder().userName("sunscreen98").name("Mike").surname("Regen").email("mikey98@gmail.com").address("Greatstreet").password("passwordhash2").admin(false).userRole(UserRole.User).build();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(artist);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
        Long artistId = artists.get(0).getId();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow1 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson1 = ow1.writeValueAsString(user);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(2, users.size());
        Long userId = users.get(1).getId();

        Artist artist1 = Artist.builder().id(artistId).userName("momo45").name("Millie").surname("Bobbington").email("mil.b@aol.de").address("Mispelstreet").password("passwordhash").admin(false).userRole(UserRole.Artist).description("Description of new artist").profileSettings("settings string").build();

        ApplicationUser user1 = ApplicationUser.builder().id(userId).userName("sunscreen98").name("Mike").surname("Regen").email("mikey98@gmail.com").address("Greatstreet").password("passwordhash2").admin(false).userRole(UserRole.User).build();

        Commission commission = Commission.builder().artist(artist1).customer(user1).sketchesShown(3).feedbackSent(0).price(300).issueDate(LocalDateTime.now()).deadlineDate(LocalDateTime.now().plusDays(20)).title("its yours").instructions("do the thing").feedbackRounds(2).build();


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/commissions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(commissionMapper.commissionToDetailedCommissionDto(commission));

        mockMvc.perform(post("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        List<SimpleCommissionDto> commissions = allCommissions();
        assertEquals(1, commissions.size());
        assertTrue(commissions.toString().contains("do the thing"));

        Long commissionId = commissions.get(0).getId();

        mockMvc.perform(get("/api/v1/commissions/" + commissionId))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();

        Commission modCommission = Commission.builder().id(commissionId).feedbackRounds(2).artist(artist1).customer(user1).sketchesShown(3).feedbackSent(0).price(300).issueDate(commission.getIssueDate()).deadlineDate(commission.getDeadlineDate()).title("Drawing french").instructions("draw me like one of your french girls").build();

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow3 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson3 = ow3.writeValueAsString(commissionMapper.commissionToDetailedCommissionDto(modCommission));

        mockMvc.perform(put("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isOk()).andReturn();

        List<SimpleCommissionDto> commissions1 = allCommissions();
        assertEquals(1, commissions1.size());
        assertFalse(commissions1.toString().contains("do the thing"));
        assertTrue(commissions1.toString().contains("draw me like one of your french girls"));

        mockMvc.perform(delete("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isOk()).andReturn();

        List<SimpleCommissionDto> commissions2 = allCommissions();
        assertEquals(0, commissions2.size());

    }*/


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
                .get("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ApplicationUserDto> userResult = objectMapper.readerFor(ApplicationUserDto.class).<ApplicationUserDto>readValues(body).readAll();
        return userResult;
    }

    public List<SimpleCommissionDto> allCommissions() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/commissions")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<SimpleCommissionDto> commissionResult = objectMapper.readerFor(SimpleCommissionDto.class).<SimpleCommissionDto>readValues(body).readAll();
        return commissionResult;
    }

}
