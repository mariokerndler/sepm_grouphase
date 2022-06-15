package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {

    private Long id;

    @ValidAlphaNumeric
    @Size(max = 100)
    @NotNull
    @NotBlank
    private String title;

    @ValidAlphaNumeric
    @Size(max = 500)
    private String message;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @NotNull
    private boolean isRead;

    @NotNull
    private NotificationType type;

    @NotNull
    private Long referenceId;

    @NotNull
    @Valid
    private Long userId;

    public NotificationDto(String title,
                           String message,
                           LocalDateTime createdAt,
                           boolean isRead,
                           NotificationType type,
                           Long referenceId,
                           Long userId) {
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.type = type;
        this.referenceId = referenceId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "NotificationDto{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", message='" + message + '\''
            + ", createdAt=" + createdAt
            + ", isRead=" + isRead
            + ", type=" + type
            + ", referenceId=" + referenceId
            + ", userId=" + userId
            + '}';
    }
}
