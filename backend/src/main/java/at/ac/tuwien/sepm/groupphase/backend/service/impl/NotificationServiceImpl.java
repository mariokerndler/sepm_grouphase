package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotificationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
import at.ac.tuwien.sepm.groupphase.backend.utils.NotificationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public List<Notification> findByUserAndIsRead(ApplicationUser user, Boolean isRead) {
        log.trace("calling findByUserAndIsRead() ...");
        var foundNotifications = this.notificationRepository.findByUserAndIsRead(user, isRead);
        log.info("Fetched all notifications ({})", foundNotifications.size());
        return foundNotifications;
    }

    @Override
    public void createNotificationByCommission(Commission oldCommission, Commission newCommission) {
        List<Notification> notifications = new ArrayList<>();

        // New candidate added
        if (oldCommission.getArtistCandidates() != null && newCommission.getArtistCandidates() != null) {
            notifications.addAll(
                addNewNotificationsIfNewCandidate(
                    oldCommission.getArtistCandidates(),
                    newCommission.getArtistCandidates(),
                    newCommission.getId()));
        }

        // New sketches uploaded

        // New feedback send

        // Status changed

        notifications.forEach(this::saveNotification);
    }

    private List<Notification> addNewNotificationsIfNewCandidate(List<Artist> oldCandidates, List<Artist> newCandidates, Long commissionId) {
        List<Notification> notifications = new ArrayList<>();
        newCandidates.removeAll(oldCandidates);

        newCandidates.forEach(artist -> notifications.add(Notification.builder()
            .title(NotificationMessages.NEW_CANDIDATE_ADDED_TITLE)
            .message(NotificationMessages.NEW_CANDIDATE_ADDED_MESSAGE)
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .trigger(NotificationTrigger.CommissionInfo)
            .referenceId(commissionId)
            .user(artist)
            .build()));

        return notifications;
    }
}
