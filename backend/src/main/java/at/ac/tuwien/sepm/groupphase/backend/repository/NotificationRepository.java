package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * An implementation of {@link JpaRepository}
 * (see <a href="https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa">
 * https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa</a>)
 * to persist & retrieve {@link Notification notification entities} in/from the application DB.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find all notifications based on the {@link ApplicationUser#getId()} and {@link Pageable page size}.
     *
     * @param applicationUserId The {@link ApplicationUser#getId()} that will be used in the search.
     * @param pageSize A limit of how many notifications will be found.
     * @return The found notifications.
     */
    @Query("SELECT n FROM Notification n where n.user.id=:applicationUserId ORDER BY n.createdAt DESC")
    List<Notification> userNotification(@Param("applicationUserId") Long applicationUserId, Pageable pageSize);

    /**
     * Find all notifications from a given {@link ApplicationUser user}.
     *
     * @param user The {@link ApplicationUser user} that will be used as a search parameter.
     * @return The found notifications.
     */
    List<Notification> findByUser(ApplicationUser user);

    /**
     * Find all notifications from a given {@link ApplicationUser user} and an {@link Notification#getId()}.
     *
     * @param user The {@link ApplicationUser user} that will be used as a search parameter.
     * @param id The {@link Notification#getId()} that will be used as a search parameter.
     * @return The found notification.
     */
    Notification findByUserAndId(ApplicationUser user, Long id);

    /**
     * Find all notifications from a given {@link ApplicationUser user} and {@link Notification#getType() type}.
     *
     * @param user The {@link ApplicationUser user} that will be used as a search parameter.
     * @param type The {@link Notification#getType() type} that will be used as a search parameter.
     * @return The found notifications.
     */
    List<Notification> findByUserAndType(ApplicationUser user, NotificationType type);

    /**
     * Find all notifications from a given {@link ApplicationUser user}, {@link Notification#getType() type} and {@link Pageable page size}.
     *
     * @param user The {@link ApplicationUser user} that will be used as a search parameter.
     * @param type The {@link Notification#getType() type} that will be used as a search parameter.
     * @param pageSize A limit of how many notifications will be found.
     * @return The found notifications.
     */
    List<Notification> findByUserAndType(ApplicationUser user, NotificationType type, Pageable pageSize);

    /**
     * Find all notifications from a given {@link ApplicationUser user} and {@link Notification#isRead()}.
     *
     * @param user The {@link ApplicationUser user} that will be used as a search parameter.
     * @param isRead The {@link Notification#isRead()} that will be used as a search parameter.
     * @return The found notifications.
     */
    List<Notification> findByUserAndIsRead(ApplicationUser user, boolean isRead);
}
