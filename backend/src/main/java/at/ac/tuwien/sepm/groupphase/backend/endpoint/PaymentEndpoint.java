package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CheckoutPaymentDto;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/payments")
public class PaymentEndpoint {

    // TODO: Change this to real endpoint key after finishing to test with cli
    private static final String endpointSecret = "whsec_bb6e0c9d52dcbe770935f0618a4aabc30223c2a3c3bde5273510edb2dbb5fc2b";

    /**
     * Payment with Stripe checkout page.
     *
     * @throws StripeException Error that is thrown in case payment fails.
     */
    @PermitAll
    @PostMapping("/checkout")
    @Operation(summary = "Authenticate and redirect to stripe checkout page")
    public Map<String, String> paymentWithCheckoutPage(@RequestBody CheckoutPaymentDto payment) throws StripeException {
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
                SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                .builder().setName(payment.getName()).build())
                            .build())
                    .build())
            .build();
        // create a stripe session
        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", session.getId());
        // We return the sessionId as a String
        return responseData;
    }

    /**
     * Payment with Stripe checkout page.
     *
     * @throws StripeException Error that is thrown in case payment fails.
     */
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/webhook")
    @Operation(summary = "Receive information from webhook after user finishes payment")
    public void receiveWebhook(HttpServletRequest request) throws StripeException, IOException {
        String payload = IOUtils.toString(request.getInputStream());
        String sigHeader = request.getHeader("Stripe-Signature");
        Event event = null;

        if (sigHeader == null) {
            // TODO: Think of appropriate error handling for stripe backend
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stripe-Signature Header is missing!");
        }

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            // Invalid signature
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        // Handle the checkout.session.completed event
        /*if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject();

            // Fulfill the purchase...
            fulfillOrder(session);
        }*/
    }

    private static void init() {
        Stripe.apiKey = "sk_test_51LCfpACibvYbUKMDNFw9N3LmJ4tzWINSvnKIw6f565GMUdbxloFSHtJlBdRvsMbCXnP0wDf3653LbrohfbiztdrB00UTaNfhbg";
    }
}