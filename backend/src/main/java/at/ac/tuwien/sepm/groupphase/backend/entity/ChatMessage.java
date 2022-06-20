package at.ac.tuwien.sepm.groupphase.backend.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fromId;
    @Column(nullable = false)
    private Long toId;


    @Column(nullable = false, length = 1000)
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, name = "sent_date")
    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "chat")
    private Chat chat;

    public ChatMessage(String message, LocalDateTime sentDate) {
        this.message = message;
        this.sentDate = sentDate;
    }

    @Override
    public String toString() {
        return "ChatMessage{"
            + "id=" + id
            + ", message='" + message + '\''
            + ", sentDate=" + sentDate
            + ", chat=" + chat.getId()
            + '}';
    }

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        ChatMessage other = (ChatMessage) obj;
        return id != null && id.equals(other.getId());
    }

}
