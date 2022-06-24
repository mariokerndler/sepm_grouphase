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
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
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
        try {
            var notifications = getNotificationsByUserAndTriggerAction(userId, notificationType, limit);

            return notifications
                .stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
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
        try {
            var user = userService.findUserById(userId);
            var notifications = notificationService.findByUserAndIsRead(user, false);

            return notifications
                .stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
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
        try {
            var notifications = getNotificationsByUserAndTriggerAction(userId, notificationType, null);

            notifications.forEach((n) -> {
                n.setRead(hasRead);
                notificationService.saveNotification(n);
            });

            return notifications
                .stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @PatchMapping("/{id}/{hasRead}")
    @Operation(summary = "Mark all notifications as read true/false by user id.")
    public NotificationDto pathNotificationIsReadOfIdFromUser(
        @PathVariable Long id,
        @PathVariable Boolean hasRead,
        @RequestParam("userId") Long userId
    ) {
        log.info("A user is trying patch the notifications from an user.");
        var user = userService.findUserById(userId);

        var notification = notificationService.findByUserAndNotificationId(user, id);

        if (notification == null) {
            var exception = new NotFoundException("Notification with id '" + id + "' from user with id '" + userId + "' not found.");
            log.error(exception.getMessage(), exception);
            throw exception;
        }

        try {
            notification.setRead(hasRead);
            notificationService.saveNotification(notification);
            return notificationMapper.notificationToNotificationDto(notification);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new notification")
    @Transactional
    public void createNotification(@RequestBody NotificationDto notificationDto) {
        log.info("A user is trying to create a notifications.");
        try {
            notificationService
                .saveNotification(notificationMapper.notificationDtoToNotification(notificationDto));
        } catch (Exception e) {
            log.error(e.getMessage() + notificationDto.toString(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping
    @Operation(summary = "Delete a notification")
    @Transactional
    public void deleteNotification(@RequestBody NotificationDto notificationDto) {
        log.info("A user is trying to delete a notification.");
        try {
            notificationService
                .deleteNotification(notificationMapper.notificationDtoToNotification(notificationDto));
        } catch (Exception e) {
            log.error(e.getMessage() + notificationDto.toString(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    private List<Notification> getNotificationsByUserAndTriggerAction(Long userId, NotificationType notificationType, Integer limit) {
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
