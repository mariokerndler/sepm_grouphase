package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistDto {

    // TODO: Add aditional artist attributes to dto

    private String userName;

    private String name;

    private String surname;

    private String email;

    private String address;

    private String password;

    private Boolean admin;

    private UserRole userRole;

    private Long id;

    public ArtistDto() {
    }

    public ArtistDto(String userName, String name, String surname, String email, String address,
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

}