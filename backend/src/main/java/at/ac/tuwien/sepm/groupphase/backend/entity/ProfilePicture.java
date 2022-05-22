package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ProfilePicture")
@Entity
public class ProfilePicture extends Image{

    @OneToOne
    private Artist artist;

    public ProfilePicture(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "ProfilePicture{" +
            "artist=" + artist +
            '}' + super.toString();
    }

    @Override
    public int hashCode() {
        return 47;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProfilePicture other = (ProfilePicture) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }
}
