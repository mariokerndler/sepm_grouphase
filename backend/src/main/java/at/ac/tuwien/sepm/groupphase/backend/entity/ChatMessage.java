package at.ac.tuwien.sepm.groupphase.backend.entity;


import lombok.*;

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

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false, name = "sent_date")
    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "chat")
    private Chat chat;

    public ChatMessage(String text, LocalDateTime sentDate) {
        this.text = text;
        this.sentDate = sentDate;
    }

    @Override
    public String toString() {
        return "ChatMessage{"
            + "id=" + id
            + ", text='" + text + '\''
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
