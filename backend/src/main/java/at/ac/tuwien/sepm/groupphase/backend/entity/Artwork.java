package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.HasId;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Artwork extends Image implements HasId {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
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
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"artwork_id", "tag_id"})})
    private List<Tag> tags;

    public Artwork(String name, String description, String imageUrl, FileType fileType, Artist artist, List<Sketch> sketches, Commission commission) {
        super(imageUrl, fileType);
        this.name = name;
        this.description = description;
        this.artist = artist;
        this.sketches = sketches;
        this.commission = commission;
    }

    public Artwork(String name, String description, String imageUrl, FileType fileType, Artist artist, List<Sketch> sketches, Commission commission, byte[] content) {
        super(imageUrl, fileType);
        this.name = name;
        this.description = description;
        this.artist = artist;
        this.sketches = sketches;
        this.commission = commission;
        this.imageData = content;
    }

    @Override
    public String toString() {
        return "Artwork{"
            + ", name='" + name + '\''
            + ", description='" + description + '\''
            + ", artist=" + artist.getId()
            + ", sketches=" + (sketches == null ? null : sketches.stream().map(Sketch::getId).toList())
            + ", commission=" + (commission == null ? null : commission.getId())
            + '}' + super.toString();
    }

    @Override
    public int hashCode() {
        return 7;
    }

    public void addTag(Tag t) {
        if (!tags.contains(t)) {
            this.tags.add(t);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Artwork other = (Artwork) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }

}
