package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import at.ac.tuwien.sepm.groupphase.backend.utils.validators.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext
    EntityManager entityManager;

    private final UserRepository userRepo;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final ImageFileManager ifm;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserValidator userValidator,
                           PasswordEncoder passwordEncoder,
                           ImageFileManager ifm) {
        this.userRepo = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.ifm = ifm;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findUserByEmail(email);
            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findUserByEmail(String email) {
        log.trace("calling findUserByEmail() ...");
        ApplicationUser applicationUser = userRepo.findApplicationUserByEmail(email);
        log.info("Found application user with email='{}'", email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the application user with the email address %s", email));
    }

    @Override
    public ApplicationUser findUserById(Long id) {
        log.trace("calling findUserById() ...");
        Optional<ApplicationUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            log.info("Found application user with id='{}'", id);
            return user.get();
        } else {
            throw new NotFoundException(String.format("Could not find application user with id %s", id));
        }
    }

    @Override
    public void updateUser(ApplicationUser user) {
        log.trace("calling updateUser() ...");
        userValidator.validateUser(user);

        ApplicationUser oldUser = findUserById(user.getId());

        // TODO: Expand functionality to renaming upp folder
        updateProfilePictureFiles(oldUser, user);

        userRepo.save(user);
        log.info("Saved application user with id='{}'", user.getId());
    }

    @Override
    public void registerUser(ApplicationUser user) {
        log.trace("calling registerUser() ...");

        userValidator.validateUser(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        updateProfilePictureFiles(null, user);

        userRepo.save(user);
        log.info("Created an application user with id='{}'", user.getId());
    }

    @Override
    public List<ApplicationUser> searchUser(Specification<ApplicationUser> spec) {
        log.trace("calling searchUser() ...");
        List<ApplicationUser> foundUsers = this.userRepo.findAll(spec);
        log.info("Retrieved all application users {} ({})",
            spec != null ? "matching the search request: " + spec : "",
            foundUsers.size());
        return foundUsers;
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        log.trace("calling getAllUsers() ...");
        List<ApplicationUser> foundUsers = userRepo.findAll();
        log.info("Retrieved all application users ({})", foundUsers.size());
        return foundUsers;
    }

    @Override
    public boolean checkIfValidOldPassword(ApplicationUser user, String oldPassword) {
        log.trace("calling checkIfValidOldPassword() ...");
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeUserPassword(ApplicationUser user, String password) {
        log.trace("calling changeUserPassword() ...");
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        log.info("Updated password for application user with id='{}'", user.getId());
    }

    @Override
    public void upgradeUserToArtist(Long id) {
        entityManager.createNativeQuery("UPDATE APPLICATION_USER SET USERTYPE = ?, USER_ROLE = ?, REVIEW_SCORE = 0 WHERE ID = ?")
            .setParameter(1, "Artist")
            .setParameter(2, 1)
            .setParameter(3, id)
            .executeUpdate();

        entityManager.flush();

        ApplicationUser user = findUserById(id);
        ifm.createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + user.getUserName());

        //TODO: delete profile picture from profile picture folder?
        if (user.getProfilePicture() != null) {
            String imageUrl = ifm.writeAndReplaceUserProfileImage(user);
            user.getProfilePicture().setImageUrl(imageUrl);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        log.trace("calling deleteUserById() ...");
        Optional<ApplicationUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            log.info(user.get().getUserName());
            // TODO: Ifm delete files of artist
            userRepo.deleteById(id);
            log.info("Deleted application user with id='{}'", id);
        } else {
            throw new NotFoundException(String.format("Could not find application user with id %s", id));
        }
    }

    public void updateProfilePictureFiles(ApplicationUser oldUser, ApplicationUser newUser) {
        log.trace("calling updateProfilePicture() ...");

        if (newUser != null && newUser.getProfilePicture() != null) {
            String imageUrl = ifm.writeAndReplaceUserProfileImage(newUser);
            newUser.getProfilePicture().setImageUrl(imageUrl);

            if (oldUser != null && oldUser.getProfilePicture() != null) {
                newUser.getProfilePicture().setId(oldUser.getProfilePicture().getId());
            }
        } else {
            ifm.deleteUserProfileImage(newUser);
        }
    }
}
