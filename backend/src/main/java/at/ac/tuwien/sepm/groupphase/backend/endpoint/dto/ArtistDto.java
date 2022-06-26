package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ApplicationUserValidationMessages;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ArtistValidationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumericWithSpaces;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class ArtistDto {

    public static final int NAME_SIZE = 50;
    public static final int ADDRESS_PASSWORD_SIZE = 100;

    private Long id;

    @ValidAlphaNumeric(message = ApplicationUserValidationMessages.USERNAME_NO_VALID_ALPHA_NUMERIC)
    @Size(max = 50, message = ApplicationUserValidationMessages.USERNAME_SIZE_TOO_BIG)
    private String userName;

    @Valid
    private ProfilePictureDto profilePictureDto;

    @ValidAlphaNumericWithSpaces(
        message = ApplicationUserValidationMessages.NAME_NO_VALID_ALPHA_NUMERIC_WITH_SPACES)
    @Size(max = NAME_SIZE,
        message = ApplicationUserValidationMessages.NAME_SIZE_TOO_BIG)
    private String name;

    @ValidAlphaNumericWithSpaces(
        message = ApplicationUserValidationMessages.SURNAME_NO_VALID_ALPHA_NUMERIC_WITH_SPACES)
    @Size(max = NAME_SIZE,
        message = ApplicationUserValidationMessages.SURNAME_SIZE_TOO_BIG)
    private String surname;

    @NotNull(message = ApplicationUserValidationMessages.EMAIL_NOT_NULL)
    @Email(message = ApplicationUserValidationMessages.EMAIL_NOT_VALID)
    @Size(max = NAME_SIZE, message = ApplicationUserValidationMessages.EMAIL_SIZE_TOO_BIG)
    private String email;

    @NotBlank(message = ApplicationUserValidationMessages.ADDRESS_NOT_BLANK)
    @Size(max = ADDRESS_PASSWORD_SIZE, message = ApplicationUserValidationMessages.ADDRESS_SIZE_TOO_BIG)
    private String address;

    @NotBlank(message = ApplicationUserValidationMessages.PASSWORD_NOT_BLANK)
    @Size(max = ADDRESS_PASSWORD_SIZE, message = ApplicationUserValidationMessages.PASSWORD_SIZE_TOO_BIG)
    private String password;

    @NotNull(message = ApplicationUserValidationMessages.ADMIN_NOT_NULL)
    private Boolean admin;

    @NotNull(message = ApplicationUserValidationMessages.USER_ROLE_NOT_NULL)
    private UserRole userRole;

    @Size(max = 255, message = ArtistValidationMessages.DESCRIPTION_SIZE_TOO_BIG)
    private String description;

    @Min(value = 0, message = ArtistValidationMessages.REVIEW_SCORE_TOO_LOW)
    @Max(value = 5, message = ArtistValidationMessages.REVIEW_SCORE_TOO_HIGH)
    private double reviewScore;

    private Long galleryId;

    private List<Long> artworksIds;

    private List<Long> commissionsIds;

    private List<Long> reviewsIds;

    @Size(max = 510, message = ArtistValidationMessages.PROFILE_SETTINGS_TOO_BIG)
    private String profileSettings;

    public ArtistDto(String userName,
                     String name,
                     String surname,
                     String email,
                     String address,
                     String password,
                     Boolean admin,
                     UserRole userRole,
                     String description,
                     double reviewScore,
                     Long galleryId,
                     List<Long> artworksIds,
                     List<Long> commissions,
                     List<Long> reviews,
                     String profileSettings) {
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.password = password;
        this.admin = admin;
        this.userRole = userRole;
        this.description = description;
        this.reviewScore = reviewScore;
        this.galleryId = galleryId;
        this.artworksIds = artworksIds;
        this.commissionsIds = commissions;
        this.reviewsIds = reviews;
        this.profileSettings = profileSettings;
    }

    @Override
    public String toString() {
        return "ArtistDto{"
            + "id=" + id
            + ", userName='" + userName + '\''
            + ", name='" + name + '\''
            + ", surname='" + surname + '\''
            + ", email='" + email + '\''
            + ", address='" + address + '\''
            + ", password='" + password + '\''
            + ", admin=" + admin
            + ", userRole=" + userRole
            + ", reviewScore=" + reviewScore
            + ", galleryId=" + galleryId
            + ", artworksIds=" + artworksIds
            + ", commissionsIds=" + commissionsIds
            + ", reviewsIds=" + reviewsIds
            + ", profileSettings='" + profileSettings + '\'' + '}';
    }

}