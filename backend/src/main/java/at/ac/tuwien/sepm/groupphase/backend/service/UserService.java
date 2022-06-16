package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Returns all users saved in the database.
     *
     * @return a list of all users.
     */
    List<ApplicationUser> getAllUsers();

    //TODO: Exception for Josef

    /**
     * Find a user in the context of Spring Security based on the email address.
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address.
     * @return a Spring Security user.
     * @throws UsernameNotFoundException is thrown if the specified user does not exist.
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Returns the user with the specified email, if there is a user with this email saved in the database.
     * Correct because email must be unique.
     *
     * @param email Rhe email to look for.
     * @return The artist with the specified email.
     */
    ApplicationUser findUserByEmail(String email);

    /**
     * Returns the user with the specified id, if there is a user with this id saved in the database.
     *
     * @param id The id to look for.
     * @return The user with the specified id.
     */
    ApplicationUser findUserById(Long id);

    /**
     * Saves the given user in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param user The user being saved.
     */
    void registerUser(ApplicationUser user);

    /**
     * Updates the given user, if there is a user with this id saved in the database.
     *
     * @param user The user being updated.
     */
    void updateUser(ApplicationUser user);

    /**
     * Returns a list of all users that fit the criteria given by spec.
     *
     * @param spec The criteria to search for.
     * @return A list of fitting users
     */
    List<ApplicationUser> searchUser(Specification<ApplicationUser> spec);

    /**
     * Returns true/false if the old password is equal to the user's password.
     *
     * @param user        The user, which password will be checked.
     * @param oldPassword The old password which will be compared to.
     * @return true if the password matches, false if not.
     */
    boolean checkIfValidOldPassword(ApplicationUser user, String oldPassword);

    /**
     * Change the password of the given user.
     *
     * @param user     The user, which password will be updated.
     * @param password The new password of the given user.
     */
    void changeUserPassword(ApplicationUser user, String password);


    /**
     * Saves the given user as an artist entity, preserving the original id.
     *
     * @param user The user to upgrade.
     */
    void upgradeUserToArtist(ApplicationUser user);

    /**
     * Deletes the user with the specified id, if there is a user with this id saved in the database.
     *
     * @param id The id of the user to be deleted.
     */
    void deleteUserById(Long id);

}
