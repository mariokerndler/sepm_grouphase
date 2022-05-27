package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Review;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
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

    private long galleryId;

    private List<Long> artworksIds;

    private List<Commission> commissions;

    private List<Review> reviews;

    @Size(max = 255)
    private String profileSettings;

    public ArtistDto(String userName, String name, String surname, String email, String address,
                     String password, Boolean admin, UserRole userRole, double reviewScore, long galleryId,
                     List<Long> artworksIds, List<Commission> commissions, List<Review> reviews,
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
        this.commissions = commissions;
        this.reviews = reviews;
        this.profileSettings = profileSettings;
    }

    public void addArtworkId(Long i){
        if(artworksIds == null){
            return;
        }

        this.artworksIds.add(i);
    }
}