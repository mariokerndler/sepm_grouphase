package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    ApplicationUser findApplicationUserByEmail(String email);

    ApplicationUser findApplicationUsersByEmailAndAdmin(String email, Boolean isAdmin);

    List<ApplicationUser> findByPasswordEquals(String password);
}
