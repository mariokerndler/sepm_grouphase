package at.ac.tuwien.sepm.groupphase.backend.utils.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@NotBlank
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidAlphaNumericValidator.class})
public @interface ValidAlphaNumeric {

    String message() default "field should not be empty and only contain alphanumeric character(s).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
