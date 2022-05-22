package at.ac.tuwien.sepm.groupphase.backend.utils.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotBlank
@Pattern(regexp = "[a-zA-Z0-9]*", message = "may only contain numbers or digits")
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface ValidAlphaNumeric {

    String message() default "field should not be empty and only contain alphanumeric character(s).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
