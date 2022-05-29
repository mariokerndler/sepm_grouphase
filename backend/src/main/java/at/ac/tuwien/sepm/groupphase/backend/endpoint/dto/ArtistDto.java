package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArtistDto {

    private Long id;

    @ValidAlphaNumeric
    @Size(max = 50)
    private String userName;

    // TODO: Should we allow more than one first name?
    @ValidAlphaNumeric
    @Size(max = 50)
    private String name;

    @ValidAlphaNumeric
    @Size(max = 50)
    private String surname;

    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String address;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotNull
    private Boolean admin;

    @NotNull
    private UserRole userRole;

    @Min(0)
    @Max(5)
    private double reviewScore;

    private Long galleryId;

    private List<Long> artworksIds;

    private List<Long> commissionsIds;

    private List<Long> reviewsIds;

    @Size(max = 255)
    private String profileSettings;

    public ArtistDto(String userName, String name, String surname, String email, String address,
                     String password, Boolean admin, UserRole userRole, double reviewScore, Long galleryId,
                     List<Long> artworksIds, List<Long> commissions, List<Long> reviews,
                     String profileSettings) {
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.password = password;
        this.admin = admin;
        this.userRole = userRole;
        this.reviewScore = reviewScore;
        this.galleryId = galleryId;
        this.artworksIds = artworksIds;
        this.commissionsIds = commissions;
        this.reviewsIds = reviews;
        this.profileSettings = profileSettings;
    }

    @Override
    public String toString() {
        return "ArtistDto{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", address='" + address + '\'' +
            ", password='" + password + '\'' +
            ", admin=" + admin +
            ", userRole=" + userRole +
            ", reviewScore=" + reviewScore +
            ", galleryId=" + galleryId +
            ", artworksIds=" + artworksIds +
            ", commissionsIds=" + commissionsIds +
            ", reviewsIds=" + reviewsIds +
            ", profileSettings='" + profileSettings + '\'' +
            '}';
    }


    //TODO: unused
    public void addArtworkId(Long i) {
        if (artworksIds == null) {
            return;
        }

        this.artworksIds.add(i);
    }
}