package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationUserDto {

    private Long id;

    private String userName;

    private String name;

    private String surname;

    private String email;

    private String address;

    private String password;

    private Boolean admin;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUserDto that = (ApplicationUserDto) o;
        return id == that.id && userName.equals(that.userName) && name.equals(that.name) && surname.equals(that.surname) && email.equals(that.email) && address.equals(that.address) && password.equals(that.password) && admin.equals(that.admin) && userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, name, surname, email, address, password, admin, userRole, id);
    }

    @Override
    public String toString() {
        return "ApplicationUserDto{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", address='" + address + '\'' +
            ", password='" + password + '\'' +
            ", admin=" + admin +
            ", userRole=" + userRole +
            '}';
    }
}
