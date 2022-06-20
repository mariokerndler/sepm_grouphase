package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CheckoutPaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;

import javax.annotation.security.PermitAll;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/payments")
public class StripeEndpoint {

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

    private static void init() {
        Stripe.apiKey = "sk_test_51LCfpACibvYbUKMDNFw9N3LmJ4tzWINSvnKIw6f565GMUdbxloFSHtJlBdRvsMbCXnP0wDf3653LbrohfbiztdrB00UTaNfhbg";
    }
}