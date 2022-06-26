package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NotificationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotificationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.NotificationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableWebMvc
@AutoConfigureWebMvc
public class NotificationEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private CommissionMapper commissionMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private ApplicationUser applicationUser;

    private Artist artist;

    public ApplicationUser getTestApplicationUser() {
        return ApplicationUser.builder()
            .userName(TestData.TEST_USER_NAME)
            .name(TestData.TEST_USER_NAME)
            .surname(TestData.TEST_USER_SURNAME)
            .email(TestData.TEST_USER_EMAIL)
            .address(TestData.TEST_USER_ADDRESS)
            .password(TestData.TEST_USER_PASSWORD)
            .admin(TestData.TEST_USER_ADMIN)
            .userRole(TestData.TEST_USER_ROLE)
            .build();
    }

    public Artist getTestArtist() {
        return Artist.builder()
            .userName(TestData.TEST_ARTIST_USERNAME)
            .name(TestData.TEST_ARTIST_NAME)
            .surname(TestData.TEST_ARTIST_SURNAME)
            .email(TestData.TEST_ARTIST_EMAIL)
            .address(TestData.TEST_ARTIST_ADDRESS)
            .password(TestData.TEST_ARTIST_PASSWORD)
            .admin(TestData.TEST_ARTIST_ADMIN)
            .userRole(TestData.TEST_ARTIST_ROLE)
            .description(TestData.TEST_ARTIST_DESCRIPTION)
            .reviewScore(TestData.TEST_ARTIST_REVIEW_SCORE)
            .build();
    }

    public Commission getTestCommission(ApplicationUser applicationUser, Artist artist) {
        return Commission.builder()
            .artist(artist)
            .customer(applicationUser)
            .status(TestData.TEST_COMMISSION_STATUS)
            .sketchesShown(TestData.TEST_COMMISSION_SKETCHES_SHOWN)
            .feedbackSent(TestData.TEST_COMMISSION_FEEDBACK_SENT)
            .price(TestData.TEST_COMMISSION_PRICE)
            .issueDate(TestData.TEST_COMMISSION_ISSUE_DATE)
            .deadlineDate(TestData.TEST_COMMISSION_DEADLINE_DATE)
            .title(TestData.TEST_COMMISSION_TITLE)
            .instructions(TestData.TEST_COMMISSION_INSTRUCTIONS)
            .build();
    }

    public Notification getTestNotification(ApplicationUser applicationUser) {
        return Notification.builder()
            .title(TestData.TEST_NOTIFICATION_TITLE)
            .createdAt(TestData.TEST_NOTIFICATION_CREATED_AT)
            .isRead(TestData.TEST_NOTIFICATION_IS_READ)
            .type(TestData.TEST_NOTIFICATION_TYPE)
            .referenceId(TestData.TEST_NOTIFICATION_REFERENCE_ID)
            .user(applicationUser).build();
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        notificationRepository.deleteAll();
        commissionRepository.deleteAll();
        userRepository.deleteAll();
        artistRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();

        ApplicationUserDto anObject = userMapper.userToUserDto(getTestApplicationUser());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anObject);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<ApplicationUserDto> users = allUsers();
        assertEquals(1, users.size());
        applicationUser = userMapper.userDtoToUser(users.get(0));

        ArtistDto artistDto = artistMapper.artistToArtistDto(getTestArtist());
        String requestJson1 = ow.writeValueAsString(artistDto);

        mockMvc.perform(post("/api/v1/artists").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1))
            .andExpect(status().isCreated()).andReturn();

        List<ArtistDto> artists = allArtists();
        assertEquals(1, artists.size());
        this.artist = artistMapper.artistDtoToArtist(artists.get(0));
    }

    @Test
    @WithMockUser()
    public void addANotification() throws Exception {
        NotificationDto notificationDto = notificationMapper.
            notificationToNotificationDto(getTestNotification(applicationUser));
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(notificationDto);

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        List<NotificationDto> notificationDtoList = allNotifications(applicationUser);
        assertAll(
            () -> assertEquals(1, notificationDtoList.size()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TITLE, notificationDtoList.get(0).getTitle()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_CREATED_AT, notificationDtoList.get(0).getCreatedAt()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_IS_READ, notificationDtoList.get(0).isRead()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TYPE, notificationDtoList.get(0).getType()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_REFERENCE_ID, notificationDtoList.get(0).getReferenceId()),
            () -> assertEquals(applicationUser.getId(), notificationDtoList.get(0).getUserId())
        );
    }

    @Test
    @WithMockUser()
    public void addANotificationButFailValidation() throws Exception {
        NotificationDto notificationDto = notificationMapper.
            notificationToNotificationDto(getTestNotification(applicationUser));
        notificationDto.setTitle("");
        notificationDto.setType(null);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(notificationDto);

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.title", is("field should not be empty and only contain alphanumeric character(s) or spaces.")))
            .andExpect(jsonPath("$.type", is("must not be null")))
            .andReturn();
    }

    @Test
    @WithMockUser()
    public void markAllNotificationByTestUserAsRead() throws Exception {
        NotificationDto notificationDto = notificationMapper.
            notificationToNotificationDto(getTestNotification(applicationUser));
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(notificationDto);

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        mockMvc.perform(patch(String.format("/api/v1/notifications/%s", true))
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", applicationUser.getId().toString()))
            .andExpect(status().isOk()).andReturn();

        List<NotificationDto> notificationDtoList = allNotifications(applicationUser);
        assertAll(
            () -> assertEquals(1, notificationDtoList.size()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TITLE, notificationDtoList.get(0).getTitle()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_CREATED_AT, notificationDtoList.get(0).getCreatedAt()),
            () -> assertEquals(true, notificationDtoList.get(0).isRead()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TYPE, notificationDtoList.get(0).getType()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_REFERENCE_ID, notificationDtoList.get(0).getReferenceId()),
            () -> assertEquals(applicationUser.getId(), notificationDtoList.get(0).getUserId())
        );
    }

    @Test
    @WithMockUser()
    public void deleteUser() throws Exception {
        NotificationDto notificationDto = notificationMapper.
            notificationToNotificationDto(getTestNotification(applicationUser));
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow2 = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson2 = ow2.writeValueAsString(notificationDto);

        mockMvc.perform(post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
            .andExpect(status().isCreated()).andReturn();

        assertEquals(1, allNotifications(applicationUser).size());
        String requestJson3 = ow2.writeValueAsString(allNotifications(applicationUser).get(0));

        mockMvc.perform(delete("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson3))
            .andExpect(status().isAccepted()).andReturn();

        assertEquals(0, allNotifications(applicationUser).size());
    }

    @Test
    @WithMockUser()
    public void notificationSentToArtistAfterUserAddsArtistToCommission() throws Exception {

        DetailedCommissionDto detailedCommissionDto = commissionMapper.
            commissionToDetailedCommissionDto(getTestCommission(applicationUser, null));
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(detailedCommissionDto);

        mockMvc.perform(post("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated()).andReturn();

        List<SimpleCommissionDto> commissionDtos = allCommissions();
        assertEquals(1, commissionDtos.size());

        detailedCommissionDto.setId(commissionDtos.get(0).getId());
        List<ArtistDto> artistCandidatesDtos = new ArrayList<>();
        artistCandidatesDtos.add(artistMapper.artistToArtistDto(artist));
        detailedCommissionDto.setArtistCandidatesDtos(artistCandidatesDtos);
        String requestJson1 = ow.writeValueAsString(detailedCommissionDto);

        mockMvc.perform(put("/api/v1/commissions").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson1))
            .andExpect(status().isOk()).andReturn();

        List<NotificationDto> notifications = allNotifications(applicationUser);

        assertAll(
            () -> assertEquals(1, notifications.size()),
            () -> assertEquals(NotificationMessages.COMMISSION_CANDIDATE_ADDED_TITLE, notifications.get(0).getTitle()),
            () -> assertEquals(false, notifications.get(0).isRead()),
            () -> assertEquals(NotificationType.COMMISSION_CANDIDATE_ADDED, notifications.get(0).getType()),
            () -> assertEquals(commissionDtos.get(0).getId(), notifications.get(0).getReferenceId()),
            () -> assertEquals(applicationUser.getId(), notifications.get(0).getUserId())
        );
    }


    public List<NotificationDto> allNotifications(ApplicationUser applicationUser) throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/notifications")
                .param("userId", applicationUser.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<NotificationDto> notifications = objectMapper.readerFor(NotificationDto.class).<NotificationDto>readValues(body).readAll();
        return notifications;
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

    public List<ArtistDto> allArtists() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/artists/")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<ArtistDto> artists = objectMapper.readerFor(ArtistDto.class).<ArtistDto>readValues(body).readAll();
        return artists;
    }

    public List<SimpleCommissionDto> allCommissions() throws Exception {
        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/api/v1/commissions/")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();
        List<SimpleCommissionDto> commissionDtos = objectMapper.readerFor(SimpleCommissionDto.class).<SimpleCommissionDto>readValues(body).readAll();
        return commissionDtos;
    }
}
