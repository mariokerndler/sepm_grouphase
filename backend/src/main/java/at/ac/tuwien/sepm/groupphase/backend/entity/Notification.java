package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Column(length = 500)
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRead;

    @Column(nullable = false)
    private NotificationType type;

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
            && getType() == that.getType()
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
            getType(),
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
            + ", type=" + type
            + ", referenceId=" + referenceId
            + ", user=" + user
            + '}';
    }
}