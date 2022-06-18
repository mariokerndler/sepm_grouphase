package at.ac.tuwien.sepm.groupphase.backend.utils.validators;

import at.ac.tuwien.sepm.groupphase.backend.utils.constraints.OneNotNull;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

@Component
public class OneNotNullValidator implements ConstraintValidator<OneNotNull, Object> {
    private String[] fields;

    @Override
    public void initialize(final OneNotNull combinedNotNull) {
        fields = combinedNotNull.fields();
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);

        return Arrays.stream(fields)
            .map(beanWrapper::getPropertyValue)
            .filter(Objects::isNull)
            .count()
            == 1;
    }
}