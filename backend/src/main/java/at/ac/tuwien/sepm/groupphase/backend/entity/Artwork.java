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
    @Column(nullable = false, length = 100)
    private String  imageUrl;
    @Column(nullable = false)
    private FileType fileType;
    //todo sketch reference

    @ManyToOne
    @JoinColumn(name="applicationUser_id",nullable = false)
    private ApplicationUser applicationUser;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

}
