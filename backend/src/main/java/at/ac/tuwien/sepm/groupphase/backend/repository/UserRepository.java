package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository<U extends ApplicationUser> extends JpaRepository<U, Long>, JpaSpecificationExecutor<ApplicationUser> {

    U findApplicationUserByEmail(String email);

    U findApplicationUsersByEmailAndAdmin(String email, Boolean isAdmin);

    List<U> findByPasswordEquals(String password);
}
