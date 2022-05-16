package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public String toString() {
        return "Gallery{" +
            "id=" + id +
            ", artist=" + artist.getId() +
            ", artworks=" + artworks.stream().map(Artwork::getId).toList() +
            '}';
    }

    @Override
    public int hashCode() {
        return 19;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Gallery other = (Gallery) obj;
        return id != null && id.equals(other.getId());
    }

}