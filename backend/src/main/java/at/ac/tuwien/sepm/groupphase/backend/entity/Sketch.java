package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sketch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false, length = 150, unique = true)
    private String imageUrl;

    @Column(nullable = false)
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name="artwork")
    private Artwork artwork;

    public Sketch(String description, String imageUrl, FileType fileType, Artwork artwork) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artwork = artwork;
    }

    @Override
    public String toString() {
        return "Sketch{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", fileType=" + fileType +
            ", artwork=" + artwork.getId() +
            '}';
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sketch other = (Sketch) obj;
        return id != null && id.equals(other.getId());
    }

}
