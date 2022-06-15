package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    List<Notification> findAllNotifications();

    List<Notification> findByUser(ApplicationUser user, Integer limit);

    Notification findByUserAndNotificationId(ApplicationUser user, Long notificationId);

    List<Notification> findByUserAndNotificationTrigger(ApplicationUser user, NotificationType type, Integer limit);

    Notification saveNotification(Notification notification);

    List<Notification> findByUserAndIsRead(ApplicationUser user, Boolean isRead);

    void createNotificationByCommission(Commission oldCommission, Commission newCommission);
}
