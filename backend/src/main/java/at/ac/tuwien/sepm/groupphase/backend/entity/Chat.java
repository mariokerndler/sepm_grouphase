package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Chat {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser customer;

    @OneToMany(mappedBy = "chat")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ChatMessage> messages;

    @Override
    public String toString() {
        return "Chat{" +
            "id=" + id +
            ", artist=" + artist.getId() +
            ", customer=" + customer.getId() +
            '}';
    }

    @Override
    public int hashCode() {
        return 11;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Chat other = (Chat) obj;
        return id != null && id.equals(other.getId());
    }

}
