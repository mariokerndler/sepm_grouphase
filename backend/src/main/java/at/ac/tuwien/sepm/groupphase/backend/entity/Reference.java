package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reference extends Image {


    @ManyToOne
    @JoinColumn(nullable = false)
    private Commission commission;

    public Reference(String imageUrl, FileType fileType, Commission commission) {
        super(imageUrl, fileType);
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "Reference{"
            + "commission=" + commission
            + '}' + super.toString();
    }

    @Override
    public int hashCode() {
        return 43;
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
        Reference other = (Reference) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }
}
