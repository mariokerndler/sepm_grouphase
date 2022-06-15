package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;

import java.time.LocalDateTime;

public final class NotificationFactory {

    public static Notification createNotification(
        NotificationType type,
        Long referenceId,
        ApplicationUser user,
        Integer amount) {
        switch (type) {
            case COMMISSION_CANDIDATE_ADDED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_CANDIDATE_ADDED_TITLE,
                    NotificationMessages.COMMISSION_CANDIDATE_ADDED_MESSAGE,
                    type,
                    referenceId,
                    user);
            }
            case COMMISSION_CANDIDATE_REMOVED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_CANDIDATE_REMOVED_TITLE,
                    NotificationMessages.COMMISSION_CANDIDATE_REMOVED_MESSAGE,
                    type,
                    referenceId,
                    user);
            }
            case COMMISSION_SKETCH_ADDED -> {
                return createNotification(
                    NotificationMessages.commissionSketchAddedTitle(amount),
                    null,
                    type,
                    referenceId,
                    user);
            }
            case COMMISSION_FEEDBACK_ADDED -> {
                return createNotification(
                    NotificationMessages.commissionFeedbackAddedTitle(amount),
                    null,
                    type,
                    referenceId,
                    user);
            }
            case COMMISSION_STATUS_CANCELLED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_STATUS_CANCELLED_TITLE,
                    null,
                    type,
                    referenceId,
                    user);
            }
            case COMMISSION_STATUS_COMPLETED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_STATUS_COMPLETED_TITLE,
                    null,
                    type,
                    referenceId,
                    user);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static Notification createNotification(
        NotificationType type,
        Long referenceId,
        ApplicationUser user) {
        return createNotification(type, referenceId, user, null);
    }


    private static Notification createNotification(
        String title,
        String message,
        NotificationType type,
        Long referenceId,
        ApplicationUser user) {
        return Notification.builder()
            .title(title)
            .message(message)
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .type(type)
            .referenceId(referenceId)
            .user(user)
            .build();
    }
}
