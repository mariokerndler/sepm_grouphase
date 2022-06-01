package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * This method returns all users saved in the database.
     *
     * @return a list of all users
     */
    List<ApplicationUser> getAllUsers();

    //TODO: Exception for Josef

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * This method returns the user with the specified email, if there is a user with this email saved in the database.
     * Correct because email must be unique.
     *
     * @param email: the email to look for
     * @return the artist with the specified email
     */
    ApplicationUser findUserByEmail(String email);

    /**
     * This method returns the user with the specified id, if there is a user with this id saved in the database.
     *
     * @param id: the id to look for
     * @return the user with the specified id
     */
    ApplicationUser findUserById(Long id);

    /**
     * This method saves the given user in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param user: the user being saved
     * @return the saved entity
     */
    void registerUser(ApplicationUser user);

    /**
     * This method updates the given user, if there is a user with this id saved in the database.
     *
     * @param user: the user being updated
     */
    void updateUser(ApplicationUser user);

    /**
     * This method returns a list of all users that fit the criteria given by spec.
     *
     * @param spec: the criteria to search for
     * @return a list of fitting users
     */
    List<ApplicationUser> searchUser(Specification<ApplicationUser> spec);

    /**
     * This method returns true/false if the old password is equal to the user's password.
     *
     * @param user: the user, which password will be checked
     * @param oldPassword: the old password which will be compared to
     * @return returns true if the password matches, false if not.
     */
    boolean checkIfValidOldPassword(ApplicationUser user, String oldPassword);

    /**
     * Change the password of the given user.
     * @param user the user, which password will be updated.
     * @param password the new password of the given user.
     */
    void changeUserPassword(ApplicationUser user, String password);

    /**
     * This method deletes the user with the specified id, if there is a user with this id saved in the database.
     *
     * @param id: the id of the user to be deleted
     */
    void deleteUserById(Long id);

}
