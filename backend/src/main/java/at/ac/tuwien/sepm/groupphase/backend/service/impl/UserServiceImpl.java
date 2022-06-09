package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final ImageFileManager ifm;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator, PasswordEncoder passwordEncoder, ImageFileManager ifm) {
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
        log.debug("Find application user by email");
        ApplicationUser applicationUser = userRepo.findApplicationUserByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public ApplicationUser findUserById(Long id) {
        log.info(ImageDataPaths.assetAbsoluteLocation);
        Optional<ApplicationUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            log.info(user.get().getUserName());
            return user.get();
        } else {
            throw new NotFoundException(String.format("Could not find User with id %s", id));
        }
    }

    @Override
    public void updateUser(ApplicationUser user) {
        log.debug("Service: Update User {}", user.toString());
        userValidator.validateUser(user);

        ApplicationUser oldUser = findUserById(user.getId());

        // TODO: What to do when user deletes profile picture ? Choose avatar to default back to
        if (user.getProfilePicture() == null) {
            user.setProfilePicture(ProfilePicture.getDefaultProfilePicture(user));
        } else if ((oldUser.getProfilePicture() == null) || oldUser.getProfilePicture().getId() != oldUser.getProfilePicture().getId()) {
            String imageUrl = ifm.writeAndReplaceUserProfileImage(user);
            user.getProfilePicture().setImageUrl(imageUrl);
        }

        userRepo.save(user);

    }

    @Override
    public void registerUser(ApplicationUser user) {
        log.debug("Service: Register User {}", user.toString());

        userValidator.validateUser(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getProfilePicture() == null) {
            user.setProfilePicture(ProfilePicture.getDefaultProfilePicture(user));
        } else {
            String imageUrl = ifm.writeAndReplaceUserProfileImage(user);
            user.getProfilePicture().setImageUrl(imageUrl);
        }

        userRepo.save(user);
    }

    @Override
    public List<ApplicationUser> searchUser(Specification<ApplicationUser> spec) {
        return this.userRepo.findAll(spec);
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public boolean checkIfValidOldPassword(ApplicationUser user, String oldPassword) {
        log.debug("Service: CheckIfValidOldPassword User {}, Password {}", user.toString(), oldPassword);
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changeUserPassword(ApplicationUser user, String password) {
        log.debug("Service: changeUserPassword User {}, Password{}", user.toString(), password);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        log.info(ImageDataPaths.assetAbsoluteLocation);
        Optional<ApplicationUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            log.info(user.get().getUserName());
            // TODO: Ifm delete files of artist
            userRepo.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Could not find User with id %s", id));
        }
    }
}
