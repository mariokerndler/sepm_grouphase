package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Sketch {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(nullable = false, length = 50)
    //TODO: do we need a name here, like in artwork? probably not because artwork references sketch, but asking for confirmation
    private String description;
    @Column(nullable = false, length = 100, unique = true)
    private String imageUrl;
    @Column(nullable = false)
    private FileType fileType;
    @ManyToOne
    @JoinColumn(name="artwork")
    private Artwork artwork;

    public Sketch() {
    }

    public Sketch(String description, String imageUrl, FileType fileType, Artwork artwork) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artwork = artwork;
    }
}
