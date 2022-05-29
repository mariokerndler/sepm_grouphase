package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository<U extends ApplicationUser> extends JpaRepository<U, Long> {

    U findApplicationUserByEmail(String email);

    U findApplicationUsersByEmailAndAdmin(String email, Boolean isAdmin);

    List<U> findByPasswordEquals(String password);

    Optional<ApplicationUser> findByUserName(String userName);
}
