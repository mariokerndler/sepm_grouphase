package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfilePicture extends Image {

    @OneToOne
    private Artist artist;


    @Override
    public String toString() {
        return "ProfilePicture{"
            + "artist=" + artist
            + '}' + super.toString();
    }

    @Override
    public int hashCode() {
        return 47;
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
        ProfilePicture other = (ProfilePicture) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }
}
