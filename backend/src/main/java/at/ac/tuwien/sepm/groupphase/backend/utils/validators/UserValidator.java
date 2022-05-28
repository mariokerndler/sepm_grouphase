package at.ac.tuwien.sepm.groupphase.backend.utils.validators;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;


@Component
public class UserValidator {
    public void validateUser(ApplicationUser user) throws ValidationException {

        if (!user.getUserName().matches("[a-zA-Z0-9]*")) {
            throw new ValidationException("User name can only consist of Letters and Numbers");
        }


    }
}
