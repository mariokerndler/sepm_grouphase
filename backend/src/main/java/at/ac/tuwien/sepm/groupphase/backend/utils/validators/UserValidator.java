package at.ac.tuwien.sepm.groupphase.backend.utils.validators;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.springframework.stereotype.Component;



@Component
public class UserValidator {

    private UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public  void validateUser(ApplicationUser user) throws ValidationException {

        if(!user.getUserName().matches("[a-zA-Z0-9]*")){
            throw  new ValidationException("User name can only consist of Letters and Numbers");
        }

        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new ValidationException("Username is already in database");
        }

        if (userRepository.findApplicationUserByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email is already in database");
        }

    }
}
