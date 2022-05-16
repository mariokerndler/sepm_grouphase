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
@Entity
@NoArgsConstructor
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany
    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
    private List<Artwork> artworks;

    public Gallery(Long id, Artist artist, List<Artwork> artworks) {
        this.id = id;
        this.artist = artist;
        this.artworks = artworks;
    }
}