package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationTrigger;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private NotificationTrigger trigger;

    @Column(nullable = false)
    private Long referenceId;

    @ManyToOne()
    private ApplicationUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification that = (Notification) o;
        return isRead() == that.isRead()
            && getId().equals(that.getId())
            & getTitle().equals(that.getTitle())
            && getMessage().equals(that.getMessage())
            && getCreatedAt().equals(that.getCreatedAt())
            && getTrigger() == that.getTrigger()
            && getReferenceId().equals(that.getReferenceId())
            && getUser().equals(that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
            getTitle(),
            getMessage(),
            getCreatedAt(),
            isRead(),
            getTrigger(),
            getReferenceId(),
            getUser());
    }

    @Override
    public String toString() {
        return "Notification{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", message='" + message + '\''
            + ", createdAt=" + createdAt
            + ", isRead=" + isRead
            + ", trigger=" + trigger
            + ", referenceId=" + referenceId
            + ", user=" + user
            + '}';
    }
}