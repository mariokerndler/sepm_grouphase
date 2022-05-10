package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
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
    @Column(nullable = false, length = 20)
    private String password;
    @Column(nullable = false, length = 100)
    private Boolean admin;
    @Column(nullable = false)
    private UserRole userRole;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

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

    public String getUserName() {return userName;}

    public void setUserName(String userName) {this.userName = userName;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public UserRole getUserRole() {return userRole;}

    public void setUserRole(UserRole userRole) {this.userRole = userRole;}

    public long getId(){ return id;}

    public void setId(long id) {this.id = id;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
