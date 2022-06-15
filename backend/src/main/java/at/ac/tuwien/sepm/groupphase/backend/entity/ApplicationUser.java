package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ApplicationUser")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "Usertype")
@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String userName;

    @OneToOne(mappedBy = "applicationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfilePicture profilePicture;

    @Column(nullable = false, length = 35)
    private String name;

    @Column(nullable = false, length = 35)
    private String surname;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private Boolean admin;

    @Column(nullable = false)
    private UserRole userRole;

    public ApplicationUser(String userName, ProfilePicture profilePicture, String name, String surname, String email, String address,
                           String password, Boolean admin, UserRole userRole) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.password = password;
        this.admin = admin;
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "ApplicationUser{"
            + "id=" + id
            + ", userName='" + userName + '\''
            + ", profilePicture=" + (profilePicture == null ? null : profilePicture.getId()) + '\''
            + ", name='" + name + '\''
            + ", surname='" + surname + '\''
            + ", email='" + email + '\''
            + ", address='" + address + '\''
            + ", admin=" + admin
            + ", userRole=" + userRole
            + '}';
    }

    @Override
    public int hashCode() {
        return 3;
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

        ApplicationUser other = (ApplicationUser) obj;
        return id != null && id.equals(other.getId());
    }

}
