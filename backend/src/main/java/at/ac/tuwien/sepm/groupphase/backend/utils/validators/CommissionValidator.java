package at.ac.tuwien.sepm.groupphase.backend.utils.validators;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CommissionValidator {

    private final CommissionRepository commissionRepository;

    public CommissionValidator(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    public void throwExceptionIfCommissionAlreadyExists(Commission commission) {
        log.trace("calling throwExceptionIfCommissionAlreadyExists() ...");
        if (commission.getId() != null) {
            Optional<Commission> commission1 = commissionRepository.findById(commission.getId());

            if (commission1.isPresent()) {
                throw new ValidationException(String.format("There is already a Commission with id %s", commission.getId()));
            }
        }
    }

    public void throwExceptionIfCommissionDoesNotExist(Commission commission) {
        log.trace("calling throwExceptionIfCommissionDoesNotExist() ...");
        if (commission.getId() != null) {
            Optional<Commission> commission1 = commissionRepository.findById(commission.getId());

            if (!commission1.isPresent()) {
                throw new NotFoundException(String.format("There is no Commission with id %s", commission.getId()));
            }
        }
    }

    public void throwExceptionIfCommissionCanNotBePayed(Commission commission) {
        // Commission not in cancelled or in progress state
        if (commission.getStatus() != CommissionStatus.AWAITING_PAYMENT) {
            throw new ValidationException("Commissions can not currently be payed!");
        }
    }
}
