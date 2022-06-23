package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.PaymentService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final CommissionService commissionService;

    public PaymentServiceImpl(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    @Override
    @Transactional
    public void updateCommissionAfterPayment(Session stripeObject) {
        Long commissionId = Long.valueOf(stripeObject.getMetadata().get("commission_id"));

        Commission commission = commissionService.findById(commissionId);

        if (commission.getStatus() == CommissionStatus.AWAITING_PAYMENT) {
            commission.setStatus(CommissionStatus.IN_PROGRESS);
        }

        commissionService.updateCommission(commission);
    }
}
