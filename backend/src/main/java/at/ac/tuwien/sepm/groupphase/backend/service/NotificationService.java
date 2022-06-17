package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;

import java.util.List;

/**
 * Provides methods to create, retrieve and generally work with {@link Notification notifications}.
 */
public interface NotificationService {

    /**
     * Returns all {@link Notification notification entities}.
     *
     * @return The list of all {@link Notification notification entities}.
     */
    List<Notification> findAllNotifications();

    /**
     * Retrieves all existing {@link Notification notification entities} from a given {@link ApplicationUser user} and limit.
     *
     * @param user The {@link ApplicationUser user} from which the notifications will be fetched.
     * @param limit A given limit on how many notifications will be fetched.
     * @return A list of all found {@link Notification notification entities}.
     */
    List<Notification> findByUser(ApplicationUser user, Integer limit);

    /**
     * Retrieve an existing {@link Notification notification entity} from a given {@link ApplicationUser user} and an {@link Notification#getId()}.
     *
     * @param user The {@link ApplicationUser user} from which the notifications will be fetched.
     * @param notificationId The {@link Notification#getId()} of the notification that will be fetched.
     * @return The found {@link Notification notification entity}.
     */
    Notification findByUserAndNotificationId(ApplicationUser user, Long notificationId);

    /**
     * Retrieve all existing {@link Notification notification entities} from a given {@link ApplicationUser user}, {@link NotificationType type} and limit.
     *
     * @param user The {@link ApplicationUser user} from which the notifications will be fetched.
     * @param type The {@link NotificationType type} of the notification.
     * @param limit A given limit on how many notifications will be fetched.
     * @return A list of all found {@link Notification notification entities}.
     */
    List<Notification> findByUserAndNotificationTrigger(ApplicationUser user, NotificationType type, Integer limit);

    /**
     * Retrieve all existing {@link Notification notification entities} from a given {@link ApplicationUser user} and {@link Notification#isRead()}.
     *
     * @param user The {@link ApplicationUser user} from which the notifications will be fetched.
     * @param isRead Filter notifications by {@link Notification#isRead()}.
     * @return A list of all found {@link Notification notification entities}.
     */
    List<Notification> findByUserAndIsRead(ApplicationUser user, Boolean isRead);

    /**
     * Creates a new {@link Notification notification entity} from an {@link Notification notification entity}.
     *
     * @param notification The {@link Notification} that contains the information according to which the {@link Notification} should be created.
     */
    void saveNotification(Notification notification);

    /**
     * Creates new {@link Notification notification entities} based on the difference between two {@link Commission commissions}.
     *
     * @param oldCommission  {@link Commission} old commission that will be compared.
     * @param newCommission {@link Commission} new commission that will be compared.
     */
    void createNotificationByCommission(Commission oldCommission, Commission newCommission);
}
