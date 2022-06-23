package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotificationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
import at.ac.tuwien.sepm.groupphase.backend.utils.NotificationFactory;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public List<Notification> findByUserAndNotificationTrigger(ApplicationUser user, NotificationType type, Integer limit) {
        log.trace("calling findByUserAndNotificationTrigger() ...");

        List<Notification> foundNotifications;
        if (limit == null) {
            foundNotifications = this.notificationRepository.findByUserAndType(user, type);
        } else {
            foundNotifications = this.notificationRepository.findByUserAndType(user, type, PageRequest.of(0, limit));
        }

        log.info("Fetched all notifications ({})", foundNotifications.size());
        return foundNotifications;
    }

    @Override
    public void saveNotification(Notification notification) {
        log.trace("calling saveNotification() ...");
        this.notificationRepository.save(notification);
        log.info("Saved notification with id='{}'", notification.getId());
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

        // Artist chosen
        if (oldCommission.getArtist() == null && newCommission.getArtist() != null) {
            notifications.add(
                NotificationFactory.createNotification(
                    NotificationType.COMMISSION_CANDIDATE_CHOSEN,
                    newCommission.getId(),
                    newCommission.getArtist()
                )
            );
        }

        // Review uploaded
        if (oldCommission.getReview() == null && newCommission.getReview() != null) {
            notifications.add(
                NotificationFactory.createNotification(
                    NotificationType.COMMISSION_REVIEW_ADDED,
                    newCommission.getId(),
                    newCommission.getArtist()
                )
            );
        }

        // Candidate added/removed
        if (oldCommission.getArtistCandidates() != null && newCommission.getArtistCandidates() != null) {
            notifications.addAll(
                addNewNotificationsIfCandidateAddedRemoved(
                    oldCommission.getArtistCandidates(),
                    newCommission.getArtistCandidates(),
                    newCommission.getId(),
                    newCommission.getCustomer()));
        }

        // New sketches uploaded
        var sketchNotification = addNewNotificationsIfSketchUploaded(
            oldCommission.getSketchesShown(),
            newCommission.getSketchesShown(),
            newCommission.getId(),
            newCommission.getCustomer());
        if (sketchNotification != null) {
            notifications.add(sketchNotification);
        }

        // New feedback send
        var feedbackNotification = addNewNotificationsIfFeedbackUploaded(
            oldCommission.getFeedbackSent(),
            newCommission.getFeedbackSent(),
            newCommission.getId(),
            newCommission.getArtist());
        if (feedbackNotification != null) {
            notifications.add(feedbackNotification);
        }

        // Status changed
        notifications.addAll(
            addNewNotificationIfStatusChanged(
                oldCommission,
                newCommission,
                newCommission.getId(),
                newCommission.getCustomer(),
                newCommission.getArtist()));

        notifications.forEach(this::saveNotification);
    }

    private List<Notification> addNewNotificationsIfCandidateAddedRemoved(
        List<Artist> oldCandidates,
        List<Artist> newCandidates,
        Long commissionId,
        ApplicationUser customer) {
        List<Notification> notifications = new ArrayList<>();
        // Added candidates
        // Need to copy list objects and use the toBuilder() from lombok to make a copy constructor.
        List<Artist> candidates = newCandidates.stream()
            .map(artist -> artist == null ? null : artist.toBuilder().build())
            .collect(Collectors.toList());

        candidates.removeAll(oldCandidates);

        candidates.forEach(candidate -> notifications.add(
            NotificationFactory.createNotification(
                NotificationType.COMMISSION_CANDIDATE_ADDED,
                commissionId,
                customer)));

        // Removed candidates
        candidates = oldCandidates.stream()
            .map(artist -> artist == null ? null : artist.toBuilder().build())
            .collect(Collectors.toList());
        candidates.removeAll(newCandidates);
        candidates.forEach(candidate -> notifications.add(
            NotificationFactory.createNotification(
                NotificationType.COMMISSION_CANDIDATE_REMOVED,
                commissionId,
                candidate)));

        return notifications;
    }

    private Notification addNewNotificationsIfSketchUploaded(
        int oldSketchesShown,
        int newSketchesShown,
        Long commissionId,
        ApplicationUser customer) {
        int sketchesDifference = newSketchesShown - oldSketchesShown;

        if (sketchesDifference <= 0) {
            return null;
        }

        return NotificationFactory.createNotification(
            NotificationType.COMMISSION_SKETCH_ADDED,
            commissionId,
            customer,
            sketchesDifference
        );
    }

    private Notification addNewNotificationsIfFeedbackUploaded(
        int oldFeedbackSent,
        int newFeedbackSent,
        Long commissionId,
        ApplicationUser artist) {
        int feedbackDifference = newFeedbackSent - oldFeedbackSent;

        if (feedbackDifference <= 0) {
            return null;
        }

        return NotificationFactory.createNotification(
            NotificationType.COMMISSION_FEEDBACK_ADDED,
            commissionId,
            artist,
            feedbackDifference
        );
    }

    private List<Notification> addNewNotificationIfStatusChanged(
        Commission oldCommission,
        Commission newCommission,
        Long commissionId,
        ApplicationUser user,
        ApplicationUser artist) {
        var notifications = new ArrayList<Notification>();

        if (oldCommission.getStatus() != newCommission.getStatus()) {
            switch (newCommission.getStatus()) {
                case CANCELLED -> {
                    notifications.add(
                        NotificationFactory.createNotification(
                            NotificationType.COMMISSION_STATUS_CANCELLED,
                            commissionId,
                            user));
                    notifications.add(
                        NotificationFactory.createNotification(
                            NotificationType.COMMISSION_STATUS_CANCELLED,
                            commissionId,
                            artist));
                }
                case IN_PROGRESS -> notifications.add(
                    NotificationFactory.createNotification(
                        NotificationType.COMMISSION_STATUS_IN_PROGRESS,
                        commissionId,
                        user
                    )
                );
                case COMPLETED -> {
                    notifications.add(
                        NotificationFactory.createNotification(
                            NotificationType.COMMISSION_STATUS_COMPLETED,
                            commissionId,
                            user));
                    notifications.add(
                        NotificationFactory.createNotification(
                            NotificationType.COMMISSION_STATUS_COMPLETED,
                            commissionId,
                            artist));
                }
                case LISTED, NEGOTIATING -> {
                }
                default -> throw new IllegalStateException("Unexpected value: " + newCommission.getStatus());
            }
        } else if (oldCommission.getStatus() == CommissionStatus.NEGOTIATING
            && newCommission.getStatus() == CommissionStatus.NEGOTIATING) {
            if (commissionInformationChanged(oldCommission, newCommission)) {
                notifications.add(
                    NotificationFactory.createNotification(
                        NotificationType.COMMISSION_STATUS_NEGOTIATING,
                        commissionId,
                        artist
                    )
                );
            }
        }

        return notifications;
    }

    private boolean commissionInformationChanged(Commission oldCommission, Commission newCommission) {
        return (
            oldCommission.getPrice() != newCommission.getPrice()
                || !oldCommission.getDeadlineDate().equals(newCommission.getDeadlineDate())
                || !oldCommission.getInstructions().equals(newCommission.getInstructions())
                || commissionReferencesChanged(oldCommission.getReferences(), newCommission.getReferences()));
    }

    private boolean commissionReferencesChanged(List<Reference> oldReferences, List<Reference> newReferences) {
        if (oldReferences == null
            && newReferences == null) {
            return false;
        }

        return Objects.requireNonNull(oldReferences).size() != Objects.requireNonNull(newReferences).size();
    }
}
