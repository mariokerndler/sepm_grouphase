package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 35, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Artwork> artworks;

    @ManyToMany(mappedBy = "tags")
    private List<Artist> artists;

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tag other = (Tag) obj;
        return id != null && id.equals(other.getId());
    }

}
