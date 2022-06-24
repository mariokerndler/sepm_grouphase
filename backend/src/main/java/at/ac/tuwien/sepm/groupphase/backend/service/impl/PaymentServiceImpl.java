package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
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
    private final NotificationService notificationService;

    public PaymentServiceImpl(CommissionService commissionService, NotificationService notificationService) {
        this.commissionService = commissionService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public void updateCommissionAfterPayment(Session stripeSession) {
        Long commissionId = Long.valueOf(stripeSession.getMetadata().get("commission_id"));

        Commission commission = commissionService.findById(commissionId);

        if (commission.getStatus() == CommissionStatus.AWAITING_PAYMENT) {
            commission.setStatus(CommissionStatus.IN_PROGRESS);
        }

        // Create notification to notify users that commission has been paid
        notificationService.createNotificationByCommissionAfterPayment(commission);

        commissionService.updateCommission(commission);
    }

    // TODO: Remove after testing
    @Override
    @Transactional
    public void updateCommissionAfterPaymentTest(Long commissionId) {
        Commission commission = commissionService.findById(commissionId);

        if (commission.getStatus() == CommissionStatus.AWAITING_PAYMENT) {
            commission.setStatus(CommissionStatus.IN_PROGRESS);
        }

        // Create notification to notify users that commission has been paid
        notificationService.createNotificationByCommissionAfterPayment(commission);

        commissionService.updateCommission(commission);
    }
}
