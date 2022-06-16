package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;

import java.time.LocalDateTime;

public final class NotificationFactory {

    private static NotificationType type;
    private static Long referenceId;
    private static ApplicationUser user;

    public static Notification createNotification(
        NotificationType type,
        Long referenceId,
        ApplicationUser user,
        Integer amount) {

        NotificationFactory.type = type;
        NotificationFactory.referenceId = referenceId;
        NotificationFactory.user = user;

        switch (type) {
            case COMMISSION_CANDIDATE_ADDED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_CANDIDATE_ADDED_TITLE);
            }
            case COMMISSION_CANDIDATE_REMOVED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_CANDIDATE_REMOVED_TITLE);
            }
            case COMMISSION_SKETCH_ADDED -> {
                return createNotification(
                    NotificationMessages.commissionSketchAddedTitle(amount));
            }
            case COMMISSION_FEEDBACK_ADDED -> {
                return createNotification(
                    NotificationMessages.commissionFeedbackAddedTitle(amount));
            }
            case COMMISSION_STATUS_CANCELLED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_STATUS_CANCELLED_TITLE);
            }
            case COMMISSION_STATUS_COMPLETED -> {
                return createNotification(
                    NotificationMessages.COMMISSION_STATUS_COMPLETED_TITLE);
            }
            case COMMISSION_STATUS_IN_PROGRESS -> {
                return createNotification(
                    NotificationMessages.COMMISSION_STATUS_IN_PROGRESS_TITLE);
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


    private static Notification createNotification(String title) {
        return Notification.builder()
            .title(title)
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .type(NotificationFactory.type)
            .referenceId(NotificationFactory.referenceId)
            .user(NotificationFactory.user)
            .build();
    }
}
