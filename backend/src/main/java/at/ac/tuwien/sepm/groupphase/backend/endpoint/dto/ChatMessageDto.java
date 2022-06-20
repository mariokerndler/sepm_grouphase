package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String message;
    private long fromId;
    private long toId;
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentDate;

    @Override
    public String toString() {
        return "ChatMessageDto{"
            + "message='" + message + '\''
            + ", fromId=" + fromId
            + ", toId=" + toId
            + ", sentDate=" + sentDate
            + '}';
    }
}
