package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
public class ApplicationUser {

    @Column(nullable = false, length = 25)
    private String userName;
    @Column(nullable = false, length = 35)
    private String name;
    @Column(nullable = false, length = 35)
    private String surname;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 100)
    private String address;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 100)
    private Boolean admin;
    @Column(nullable = false)
    private UserRole userRole;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @OneToMany(mappedBy = "applicationUser")
    private List<Artwork> artworks;

    public ApplicationUser() {
    }

    public ApplicationUser(String userName, String name, String surname, String email, String address,
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

    public ApplicationUser(String email, String password, Boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

}
