package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NotificationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NotificationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/notifications")
public class NotificationEndpoint {

    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationEndpoint(
        NotificationService notificationService,
        UserService userService,
        NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @GetMapping
    @Operation(summary = "Get all notifications by user id")
    public List<NotificationDto> getNotificationsByUserId(
        @RequestParam("userId") Long userId,
        @RequestParam(value = "notificationType", required = false) NotificationType notificationType,
        @RequestParam(value = "limit", required = false, defaultValue = "") Integer limit) {
        log.info("A user is trying fetch all notifications from an user.");
        var notifications = getNotificationsByUserAndTriggerAction(userId, notificationType, limit);

        return notifications
            .stream()
            .map(notificationMapper::notificationToNotificationDto)
            .collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @GetMapping("/unread")
    @Operation(summary = "Get all unread notifications by user id.")
    public List<NotificationDto> getUnreadNotificationsByUserId(
        @RequestParam("userId") Long userId
    ) {
        log.info("A user is trying to fetch all unread notifications from an user.");

        var user = userService.findUserById(userId);
        var notifications = notificationService.findByUserAndIsRead(user, false);

        return notifications
            .stream()
            .map(notificationMapper::notificationToNotificationDto)
            .collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @PatchMapping("/{hasRead}")
    @Operation(summary = "Mark all notifications as read true/false by user id and trigger action.")
    public List<NotificationDto> patchNotificationsIsRead(
        @RequestParam("userId") Long userId,
        @RequestParam(value = "notificationType", required = false) NotificationType notificationType,
        @PathVariable Boolean hasRead) {
        log.info("A user is trying patch all notifications from an user.");

        var notifications = getNotificationsByUserAndTriggerAction(userId, notificationType, null);

        notifications.forEach((n) -> {
            n.setRead(hasRead);
            notificationService.saveNotification(n);
        });

        return notifications
            .stream()
            .map(notificationMapper::notificationToNotificationDto)
            .collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @PatchMapping("/{id}/{hasRead}")
    @Operation(summary = "Mark notification with id as read true/false.")
    public NotificationDto patchNotificationWithIdIsRead(
        @PathVariable Long id,
        @PathVariable Boolean hasRead
    ) {
        log.info(String.format("A user is trying patch the notification with id %s.", id.toString()));

        var notification = notificationService.findByNotificationId(id);

        notification.setRead(hasRead);
        notificationService.saveNotification(notification);
        return notificationMapper.notificationToNotificationDto(notification);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new notification")
    @Transactional
    public void createNotification(@RequestBody NotificationDto notificationDto) {
        log.info("A user is trying to create a notifications.");
        notificationService
            .saveNotification(notificationMapper.notificationDtoToNotification(notificationDto));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping
    @Operation(summary = "Delete a notification")
    @Transactional
    public void deleteNotification(@Valid @RequestBody NotificationDto notificationDto) {
        log.info("A user is trying to delete a notification.");

        notificationService
            .deleteNotification(notificationMapper.notificationDtoToNotification(notificationDto));
    }


    private List<Notification> getNotificationsByUserAndTriggerAction(
        Long userId,
        NotificationType notificationType,
        Integer limit) {

        var user = userService.findUserById(userId);

        List<Notification> notifications;

        if (notificationType == null) {
            notifications = notificationService.findByUser(user, limit);
        } else {
            notifications = notificationService.findByUserAndNotificationTrigger(user, notificationType, limit);
        }

        if (notifications == null) {
            var exception = new NotFoundException("Notifications from user with id '" + userId + "'"
                + (notificationType != null ? (" and notification type '" + notificationType) : " ")
                + "could not be found.");
            log.error(exception.getMessage(), exception);
            throw exception;
        }

        return notifications;
    }
}
