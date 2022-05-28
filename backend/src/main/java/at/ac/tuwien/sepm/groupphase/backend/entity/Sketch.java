package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sketch extends Image {

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "artwork")
    private Artwork artwork;

    public Sketch(String description, String imageUrl, FileType fileType, Artwork artwork) {
        super(imageUrl, fileType);
        this.description = description;
        this.artwork = artwork;
    }

    @Override
    public String toString() {
        return "Sketch{"
            + "description='" + description + '\''
            + ", artwork=" + artwork.getId()
            + '}' + super.toString();
    }

    @Override
    public int hashCode() {
        return 31;
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
        Sketch other = (Sketch) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }

}
