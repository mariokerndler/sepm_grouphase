package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationTrigger;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;

import java.time.LocalDateTime;

public final class NotificationFactory {

    public static Notification createNotification(
        NotificationType type,
        NotificationTrigger trigger,
        Long referenceId,
        ApplicationUser user) {
        switch (type) {
            case COMMISSION_CANDIDATE_ADDED -> {
                return createNotification(
                    NotificationMessages.NEW_CANDIDATE_ADDED_TITLE,
                    NotificationMessages.NEW_CANDIDATE_ADDED_MESSAGE,
                    trigger,
                    referenceId,
                    user);
            }
            case COMMISSION_CANDIDATE_REMOVED -> {
                return createNotification(
                    NotificationMessages.CANDIDATE_REMOVED_TITLE,
                    NotificationMessages.CANDIDATE_REMOVED_MESSAGE,
                    trigger,
                    referenceId,
                    user);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private static Notification createNotification(
        String title,
        String message,
        NotificationTrigger trigger,
        Long referenceId,
        ApplicationUser user) {
        return Notification.builder()
            .title(title)
            .message(message)
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .trigger(trigger)
            .referenceId(referenceId)
            .user(user)
            .build();
    }
}
