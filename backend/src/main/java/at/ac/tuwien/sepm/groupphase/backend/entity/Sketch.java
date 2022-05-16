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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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
}
