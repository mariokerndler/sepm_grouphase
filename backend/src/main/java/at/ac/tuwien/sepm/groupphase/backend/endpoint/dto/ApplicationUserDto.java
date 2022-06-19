package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler.ApplicationUserValidationMessages;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumeric;
import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumericWithSpaces;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;


@NoArgsConstructor
@Getter
@Setter
@Validated
public class ApplicationUserDto {

    public static final int NAME_SIZE = 50;
    public static final int ADDRESS_PASSWORD_SIZE = 100;

    private Long id;

    @ValidAlphaNumeric(message = ApplicationUserValidationMessages.USERNAME_NO_VALID_ALPHA_NUMERIC)
    @Size(max = NAME_SIZE, message = ApplicationUserValidationMessages.USERNAME_SIZE_TOO_BIG)
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

    public ApplicationUserDto(String userName, String name, String surname, String email, String address,
                              String password, Boolean admin, UserRole userRole) {
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.password = password;
        this.admin = admin;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationUserDto that = (ApplicationUserDto) o;
        return Objects.equals(id, that.id)
            && userName.equals(that.userName)
            && name.equals(that.name)
            && surname.equals(that.surname)
            && email.equals(that.email)
            && address.equals(that.address)
            && password.equals(that.password)
            && admin.equals(that.admin)
            && userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, name, surname, email, address, password, admin, userRole, id);
    }

    @Override
    public String toString() {
        return "ApplicationUserDto{"
            + "id=" + id
            + ", userName='" + userName + '\''
            + ", name='" + name + '\''
            + ", surname='" + surname + '\''
            + ", email='" + email + '\''
            + ", address='" + address + '\''
            + ", password='" + password + '\''
            + ", admin=" + admin
            + ", userRole=" + userRole
            + '}';
    }
}
