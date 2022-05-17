package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false, length = 250, unique = true)
    private String imageUrl;

    @Column(nullable = false)
    private FileType fileType;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @OneToMany(mappedBy = "artwork")
    private List<Sketch> sketches;

    @OneToOne
    private Commission commission;

    public Artwork(String name, String description, String imageUrl, FileType fileType, Artist artist, List<Sketch> sketches, Commission commission) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artist = artist;
        this.sketches = sketches;
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "Artwork{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", fileType=" + fileType +
            ", artist=" + artist.getId() +
            ", sketches=" + sketches.stream().map(Sketch::getId).toList() +
            ", commission=" + commission.getId() +
            '}';
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Artwork other = (Artwork) obj;
        return id != null && id.equals(other.getId());
    }

}
