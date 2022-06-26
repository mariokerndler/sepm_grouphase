package at.ac.tuwien.sepm.groupphase.backend.utils.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@NotBlank
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmailValidator.class})
public @interface UniqueEmail {

    String message() default "Email already in use, please choose a different one!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}