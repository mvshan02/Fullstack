package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public String createSession(List<PaymentRequest.CartItem> items, List<Long> quantities) throws StripeException {
        Stripe.apiKey = secretKey;

        SessionCreateParams.Builder builder = new SessionCreateParams.Builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/success")
                .setCancelUrl("http://localhost:5173/cancel");

        for (int i = 0; i < items.size(); i++) {
            builder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(String.valueOf(items.get(i)))
                                            .build())
                                    .setUnitAmount(2000L) // $20.00
                                    .build())
                            .setQuantity(quantities.get(i))
                            .build());
        }

        SessionCreateParams params = builder.build();
        Session session = Session.create(params);

        return session.getUrl();
    }
}
