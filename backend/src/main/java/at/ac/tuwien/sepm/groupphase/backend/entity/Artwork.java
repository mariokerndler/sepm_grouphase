package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Artwork {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String description;
    @Column(nullable = false, length = 100,unique = true)
    private String  imageUrl;
    @Column(nullable = false)
    private FileType fileType;
    //todo sketch reference

    public Artwork(String name, String description, String imageUrl, FileType fileType, Artist artist) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.fileType = fileType;
        this.artist = artist;
    }
    public  Artwork() {
        this.id=-1;
    }

    @ManyToOne
    @JoinColumn(name = "artist_id",nullable = false)
    private Artist artist;

}
