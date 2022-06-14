package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationTrigger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n where n.user.id=:applicationUserId ORDER BY n.createdAt DESC")
    List<Notification> userNotification(@Param("applicationUserId") Long applicationUserId, Pageable pageSize);

    List<Notification> findByUser(ApplicationUser user);

    Notification findByUserAndId(ApplicationUser user, Long id);

    List<Notification> findByUserAndTrigger(ApplicationUser user, NotificationTrigger trigger);

    List<Notification> findByUserAndTrigger(ApplicationUser user, NotificationTrigger trigger, Pageable pageSize);

    List<Notification> findByUserAndIsRead(ApplicationUser user, boolean isRead);
}
