package at.ac.tuwien.sepm.groupphase.backend.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatMessage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false, name = "sent_date")
    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(name="chat")
    private Chat chat;

    public ChatMessage(String text, LocalDateTime sentDate) {
        this.text = text;
        this.sentDate = sentDate;
    }
}
