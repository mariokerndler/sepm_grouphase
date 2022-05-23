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
public class Artwork extends Image{

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @OneToMany(mappedBy = "artwork")
    private List<Sketch> sketches;

    @OneToOne
    private Commission commission;

    @ManyToMany
    @JoinTable(
        name = "artwork_tag",
        joinColumns = @JoinColumn(name = "artwork_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
   private List<Tag> tags;

    public Artwork(String name, String description, String imageUrl, FileType fileType, Artist artist, List<Sketch> sketches, Commission commission) {
        super(imageUrl, fileType);
        this.name = name;
        this.description = description;
        this.artist = artist;
        this.sketches = sketches;
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "Artwork{" +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            (artist != null ? ", artist=" + artist.getId() : "") +
            (sketches != null ? ", sketches=" + sketches.stream().map(Sketch::getId).toList() : "") +
            (commission != null ? ", commission=" + commission.getId() : "") +
            '}' + super.toString();
    }

    @Override
    public int hashCode() {
        return 7;
    }

    public  void addTag(Tag t){
        if(!tags.contains(t)) {
            this.tags.add(t);
        }
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
        return this.getId() != null && this.getId().equals(other.getId());
    }

}
