package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NotificationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NotificationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationTrigger;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        @RequestParam(value = "triggerAction", required = false) NotificationTrigger triggerAction,
        @RequestParam(value = "limit", required = false, defaultValue = "") Integer limit) {
        log.info("A user is trying fetch all notifications from an user.");
        try {
            var user = userService.findUserById(userId);

            List<Notification> notifications;

            if (triggerAction == null) {
                notifications = notificationService.findByUser(user, limit);
            } else {
                notifications = notificationService.findByUserAndNotificationTrigger(user, triggerAction, limit);
            }

            return notifications
                .stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new notification")
    @Transactional
    public NotificationDto createNotification(@RequestBody NotificationDto notificationDto) {
        log.info("A user is trying to create a notifications.");
        try {
            return notificationMapper
                .notificationToNotificationDto(notificationService
                    .saveNotification(notificationMapper
                        .notificationDtoToNotification(notificationDto)));
        } catch (Exception e) {
            log.error(e.getMessage() + notificationDto.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
