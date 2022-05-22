package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Review;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class ArtistDto {

    private Long id;

    @ValidAlphaNumeric
    private String userName;

    // TODO: Should we allow more than one first name?
    @ValidAlphaNumeric
    private String name;

    @ValidAlphaNumeric
    private String surname;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String password;

    @NotNull
    private Boolean admin;

    @NotNull
    private UserRole userRole;

    @Min(0)
    @Max(5)
    private double reviewScore;

    private long galleryId;

    private List<Artwork> artworks;

    private List<Commission> commissions;

    private List<Review> reviews;

    private String artistSettings;

    public ArtistDto() {
    }

    public ArtistDto(String userName, String name, String surname, String email, String address,
                     String password, Boolean admin, UserRole userRole, double reviewScore, long galleryId,
                     List<Artwork> artworks, List<Commission> commissions, List<Review> reviews, String artistSettings) {
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
        this.artworks = artworks;
        this.commissions = commissions;
        this.reviews = reviews;
        this.artistSettings = artistSettings;

    }

}