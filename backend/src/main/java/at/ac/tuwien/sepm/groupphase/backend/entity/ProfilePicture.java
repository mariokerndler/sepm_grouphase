package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfilePicture extends Image {

    @OneToOne
    @JoinColumn(nullable = false)
    private ApplicationUser applicationUser;

    public ProfilePicture(String imageUrl, FileType fileType, ApplicationUser applicationUser) {
        super(imageUrl, fileType);
        this.applicationUser = applicationUser;
    }

    @Override
    public String toString() {
        return "ProfilePicture{"
            + "user=" + applicationUser
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

    public static ProfilePicture getDefaultProfilePicture(ApplicationUser applicationUser) {
        return ProfilePicture.builder()
            .fileType(FileType.PNG)
            .imageUrl(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.defaultUserProfilePictureLocation)
            .applicationUser(applicationUser)
            .build();
    }
}
