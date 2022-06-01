package at.ac.tuwien.sepm.groupphase.backend.utils.validators;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommissionValidator {

    private final CommissionRepository commissionRepository;

    public CommissionValidator(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    public void throwExceptionIfCommissionAlreadyExists(Commission commission) {
        if (commission.getId() != null) {
            Optional<Commission> commission1 = commissionRepository.findById(commission.getId());

            if (commission1.isPresent()) {
                throw new ValidationException(String.format("There is already a Commission with id %s", commission.getId()));
            }
        }
    }

    public void throwExceptionIfCommissionDoesNotExist(Commission commission) {
        if (commission.getId() != null) {
            Optional<Commission> commission1 = commissionRepository.findById(commission.getId());

            if (!commission1.isPresent()) {
                throw new NotFoundException(String.format("There is no Commission with id %s", commission.getId()));
            }
        }
    }
}
