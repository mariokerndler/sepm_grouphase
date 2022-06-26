package at.ac.tuwien.sepm.groupphase.backend.utils.constraints;

import at.ac.tuwien.sepm.groupphase.backend.utils.validators.ValidAlphaNumericWithSpacesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@NotBlank
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidAlphaNumericWithSpacesValidator.class})
public @interface ValidAlphaNumericWithSpaces {

    String message() default "field should not be empty and only contain alphanumeric character(s) or spaces.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
