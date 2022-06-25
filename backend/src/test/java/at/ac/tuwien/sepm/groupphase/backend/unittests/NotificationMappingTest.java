package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NotificationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NotificationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class NotificationMappingTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationMapper notificationMapper;

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

    public Notification getTestNotification(ApplicationUser applicationUser) {
        return Notification.builder()
            .title(TestData.TEST_NOTIFICATION_TITLE)
            .createdAt(TestData.TEST_NOTIFICATION_CREATED_AT)
            .isRead(TestData.TEST_NOTIFICATION_IS_READ)
            .type(TestData.TEST_NOTIFICATION_TYPE)
            .referenceId(TestData.TEST_NOTIFICATION_REFERENCE_ID)
            .user(applicationUser).build();
    }

    public NotificationDto getTestNotificationDto(Long userId) {
        return new NotificationDto(TestData.TEST_NOTIFICATION_TITLE,
            TestData.TEST_NOTIFICATION_CREATED_AT,
            TestData.TEST_NOTIFICATION_IS_READ,
            TestData.TEST_NOTIFICATION_TYPE,
            TestData.TEST_NOTIFICATION_REFERENCE_ID,
            userId);
    }

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenUser_whenMapNotificationDtoToEntity_thenEntityHasAllProperties() {
        ApplicationUser user = getTestApplicationUser();
        userRepository.save(user);

        NotificationDto notificationDto = getTestNotificationDto(user.getId());
        Notification notification = notificationMapper.notificationDtoToNotification(notificationDto);

        assertAll(
            () -> assertEquals(null, notification.getId()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TITLE, notification.getTitle()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_CREATED_AT, notification.getCreatedAt()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_IS_READ, notification.isRead()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TYPE, notification.getType()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_REFERENCE_ID, notification.getReferenceId()),
            () -> assertEquals(user.getId(), notification.getUser().getId())
        );
    }

    @Test
    @Transactional
    public void givenNothing_whenMapNotificationToDto_thenDtoHasAllProperties() {
        ApplicationUser user = getTestApplicationUser();
        userRepository.save(user);

        Notification notification = getTestNotification(user);
        NotificationDto notificationDto = notificationMapper.notificationToNotificationDto(notification);

        assertAll(
            () -> assertEquals(null, notificationDto.getId()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TITLE, notificationDto.getTitle()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_CREATED_AT, notificationDto.getCreatedAt()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_IS_READ, notificationDto.isRead()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_TYPE, notificationDto.getType()),
            () -> assertEquals(TestData.TEST_NOTIFICATION_REFERENCE_ID, notificationDto.getReferenceId()),
            () -> assertEquals(user.getId(), notificationDto.getUserId())
        );
    }

}
