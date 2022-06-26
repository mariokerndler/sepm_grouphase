package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CheckoutPaymentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.exception.PaymentException;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.PaymentService;
import at.ac.tuwien.sepm.groupphase.backend.utils.validators.CommissionValidator;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/payments")
public class PaymentEndpoint {

    // TODO: Change this to real endpoint key after finishing to test with cli
    private static final String webhookEndpointSecret = "whsec_bb6e0c9d52dcbe770935f0618a4aabc30223c2a3c3bde5273510edb2dbb5fc2b";
    private final CommissionService commissionService;
    private final CommissionValidator commissionValidator;
    private final PaymentService paymentService;

    public PaymentEndpoint(CommissionService commissionService, CommissionValidator commissionValidator, PaymentService paymentService) {
        this.commissionService = commissionService;
        this.commissionValidator = commissionValidator;
        this.paymentService = paymentService;
    }

    /**
     * Payment with Stripe checkout page. Creates Stripe session and returns sessionID to frontend.
     */
    @PermitAll
    @PostMapping("/checkout")
    @Operation(summary = "Authenticate and redirect to stripe checkout page")
    public Map<String, String> paymentWithCheckoutPage(@RequestBody CheckoutPaymentDto payment) {

        Commission commission = commissionService.findById(payment.getCommissionId());

        // Check that commission can be paid for
        commissionValidator.throwExceptionIfCommissionCanNotBePayed(commission);

        // TODO: Differentiate between big and small commissions concerning price and name ?
        boolean bigCommission = commission.getPrice() > 100;
        long priceToBePaid;
        if (commission.getFeedbackRounds() == 0) {
            priceToBePaid = (long) (commission.getPrice() * 100);
        } else {
            if (bigCommission) {
                priceToBePaid = (long) (commission.getPrice() * 100) / commission.getFeedbackRounds();
            } else {
                priceToBePaid = (long) (commission.getPrice() * 100);
            }
        }

        // We initialize stripe object with the api key
        init();
        // We create a stripe session parameters object
        SessionCreateParams params = SessionCreateParams.builder()
            // We will use the credit card payment method
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(payment.getSuccessUrl())
            .setCancelUrl(
                payment.getCancelUrl())
            .addLineItem(
                SessionCreateParams.LineItem.builder().setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(payment.getCurrency()).setUnitAmount(priceToBePaid)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData
                                    .builder().setName((bigCommission ? "Partial payment of: " : "Complete payment of: ") + commission.getTitle()).build())
                            .build())
                    .setDescription(commission.getInstructions())
                    .build())
            .putMetadata("commission_id", commission.getId().toString())
            .build();
        // create a stripe session
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException ex) {
            throw new PaymentException(ex.getMessage(), ex);
        }
        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", session.getId());
        // We return the sessionId as a String
        return responseData;
    }

    /**
     * Webhook to handle checkout session completed event. If checkout was completed and
     * commission was paid for, the method calls PaymentService method updateCommissionAfterPayment.
     * The session saves the commission id in its metadata, so webhooks can easily reference it.
     */
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/webhook")
    @Operation(summary = "Receive information from webhook after user finishes payment")
    public void receiveWebhook(HttpServletRequest request) {
        String sigHeader = request.getHeader("Stripe-Signature");
        String payload;
        Event event;

        if (sigHeader == null) {
            throw new PaymentException("Webhook could not parse request! Stripe-Signature Header is missing!");
        }

        // Read request body
        try {
            payload = IOUtils.toString(request.getInputStream());
        } catch (IOException e) {
            throw new PaymentException(e.getMessage(), e);
        }

        // Construct stripe event
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookEndpointSecret);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            throw new PaymentException(e.getMessage(), e);
        }

        // Handle the checkout.session.completed event
        if ("checkout.session.completed".equals(event.getType())) {
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            if (dataObjectDeserializer.getObject().isPresent()) {
                StripeObject stripeObject = dataObjectDeserializer.getObject().get();
                paymentService.updateCommissionAfterPayment((Session) stripeObject);
            } else {
                throw new PaymentException(String.format("Webhook was unable to deserialize event data object for %s", event));
            }
        }
    }

    private static void init() {
        Stripe.apiKey = "sk_test_51LCfpACibvYbUKMDNFw9N3LmJ4tzWINSvnKIw6f565GMUdbxloFSHtJlBdRvsMbCXnP0wDf3653LbrohfbiztdrB00UTaNfhbg";
    }
}