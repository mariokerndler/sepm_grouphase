package at.ac.tuwien.sepm.groupphase.backend.utils.validators;

import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.ValidAlphaNumericWithSpaces;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Slf4j
public class ValidAlphaNumericWithSpacesValidator implements ConstraintValidator<ValidAlphaNumericWithSpaces, String> {

    private static final Pattern alphaNumericWithSpacesPattern = Pattern.compile("^[a-zA-Z0-9 ]*$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.trace("calling isValid() ...");
        return alphaNumericWithSpacesPattern.matcher(value).find();
    }
}
