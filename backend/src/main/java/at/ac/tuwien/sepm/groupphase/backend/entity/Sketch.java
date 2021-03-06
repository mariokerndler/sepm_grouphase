package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sketch extends Image {

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String customerFeedback;

    @ManyToOne
    @JoinColumn(nullable = false, name = "artwork")
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
            + '}';
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
