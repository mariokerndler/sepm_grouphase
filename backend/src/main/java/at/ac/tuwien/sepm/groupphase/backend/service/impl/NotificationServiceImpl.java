package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotificationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> findAllNotifications() {
        log.trace("calling findAllNotifications() ...");
        var foundNotifications = this.notificationRepository.findAll();
        log.info("Fetched all notifications ({})", foundNotifications.size());
        return foundNotifications;
    }

    @Override
    public List<Notification> findByUser(ApplicationUser user, Integer limit) {
        log.trace("calling findByUser() ...");

        List<Notification> foundNotifications;
        if (limit == null) {
            foundNotifications = this.notificationRepository.findByUser(user);
        } else {
            foundNotifications = this.notificationRepository.userNotification(user.getId(), PageRequest.of(0, limit));
        }

        log.info("Fetched all notifications ({})", foundNotifications.size());
        return foundNotifications;
    }

    @Override
    public Notification findByUserAndNotificationId(ApplicationUser user, Long notificationId) {
        log.trace("calling findByUserAndNotificationId() ...");
        var foundNotifications = this.notificationRepository.findByUserAndId(user, notificationId);
        log.info("Fetched the notification with id='{}' and userId='{}'", notificationId, user.getId());
        return foundNotifications;
    }

    @Override
    public List<Notification> findByUserAndNotificationTrigger(ApplicationUser user, NotificationTrigger trigger, Integer limit) {
        log.trace("calling findByUserAndNotificationTrigger() ...");

        List<Notification> foundNotifications;
        if (limit == null) {
            foundNotifications = this.notificationRepository.findByUserAndTrigger(user, trigger);
        } else {
            foundNotifications = this.notificationRepository.findByUserAndTrigger(user, trigger, PageRequest.of(0, limit));
        }

        log.info("Fetched all notifications ({})", foundNotifications.size());
        return foundNotifications;
    }

    @Override
    public Notification saveNotification(Notification notification) {
        log.trace("calling saveNotification() ...");
        var savedNotification = this.notificationRepository.save(notification);
        log.info("Saved notification with id='{}'", notification.getId());
        return savedNotification;
    }
}
