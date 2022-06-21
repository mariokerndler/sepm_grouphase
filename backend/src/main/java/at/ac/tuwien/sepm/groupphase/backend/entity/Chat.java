package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "chat_partner_id"})
})
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser chatPartner;

    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages;


    public Chat(ApplicationUser user, ApplicationUser chatPartner) {
        this.user = user;
        this.chatPartner = chatPartner;
    }

    @Override
    public String toString() {
        return "Chat{"
            + "id=" + id
            + ", user=" + user
            + ", chatPartner=" + chatPartner
            + ", messages=" + messages + '}';
    }

    @Override
    public int hashCode() {
        return 11;
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
        Chat other = (Chat) obj;
        return id != null && id.equals(other.getId());
    }

    public ApplicationUser getUser() {
        return user;
    }

    public ApplicationUser getChatPartner() {
        return chatPartner;
    }

    public void setUser(ApplicationUser user) {
        log.info("Setting user" + user.getUserName());
        this.user = user;


    }

    public void setChatPartner(ApplicationUser chatPartner) {
            log.info("Setting ChatPartner" + chatPartner.getUserName());
            this.chatPartner = chatPartner;
    }
}
