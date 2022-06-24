package at.ac.tuwien.sepm.groupphase.backend.service;

import com.stripe.model.checkout.Session;

public interface PaymentService {

    /**
     * Updates commission after successful payment.
     */
    void updateCommissionAfterPayment(Session stripeSession);

    // TODO: Remove after testing
    void updateCommissionAfterPaymentTest(Long commissionId);
}
