package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.List;

//TODO: replace this class with a correct ApplicationUser Entity implementation
@Entity
public class ApplicationUser {


    @Column(nullable= false, length = 100)
    private String email;
    @Column(nullable= false, length = 100)
    private String password;
    @Column(nullable= false, length = 100)
    private Boolean admin;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;





    public ApplicationUser() {
    }

    public ApplicationUser(String email, String password, Boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

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
