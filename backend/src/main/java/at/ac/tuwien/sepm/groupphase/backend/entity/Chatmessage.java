package at.ac.tuwien.sepm.groupphase.backend.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Chatmessage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false, name = "sent_date")
    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(name="chat", nullable=false)
    private Chat chat;


    //TODO: do we have to save which chat this belongs to? or just reference in chat?

    public Chatmessage() {
    }

    public Chatmessage(String text, LocalDateTime sentDate) {
        this.text = text;
        this.sentDate = sentDate;
    }
}
