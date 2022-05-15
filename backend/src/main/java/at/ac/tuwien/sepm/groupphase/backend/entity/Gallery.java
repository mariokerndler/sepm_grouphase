package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    // TODO: Can not use list of strings, what is good way of storing gallery settings ?
    @Column(nullable = false)
    private String GallerySettings;

    @OneToMany
    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
    private List<Artwork> artworks;

    public Gallery() {
    }

    public Gallery(Long id, Artist artist, String gallerySettings, List<Artwork> artworks) {
        this.id = id;
        this.artist = artist;
        GallerySettings = gallerySettings;
        this.artworks = artworks;
    }
}