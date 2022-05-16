package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
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
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;
    @Column(nullable = false, length = 100, unique = true)
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

}
