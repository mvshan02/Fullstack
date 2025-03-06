package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.*;
import com.example.ElectroMart.Repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final ProductRepository productRepository; // Inject ProductRepository
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public PaymentController(ProductRepository productRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequest request) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // Fetch product prices from database
            List<SessionCreateParams.LineItem> lineItems = request.getItems().stream()
                    .map(item -> {
                        Optional<Product> productOpt = productRepository.findById(item.getProductId());

                        if (productOpt.isEmpty()) {
                            throw new RuntimeException("Product not found: " + item.getProductId());
                        }

                        Product product = productOpt.get();
                        double price = product.getPrice(); // ✅ Fetching price from DB

                        return SessionCreateParams.LineItem.builder()
                                .setQuantity((long) item.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount((long) (price * 100)) // ✅ Convert price to cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(product.getName()) // ✅ Use actual product name
                                                                .build()
                                                )
                                                .build()
                                )
                                .build();
                    })
                    .toList();

            // Create Stripe session
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/payment-success?session_id={CHECKOUT_SESSION_ID}")

                    .setCancelUrl("http://localhost:5173/cancel")   // ✅ Redirect to cancel page
                    .addAllLineItem(lineItems)
                    .build();

            Session session = Session.create(params);

            // Return session URL to frontend
            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/payment-success")
    public ResponseEntity<?> handlePaymentSuccess(@RequestBody PaymentSuccessRequest request) {
        if (request.getCartItems() == null) {
            return ResponseEntity.badRequest().body("Cart items cannot be null");
        }
        String userId = request.getUserId();
        List<CartItem> cartItems = request.getCartItems().stream()
                .map(item -> new CartItem(item.getProductId(), item.getQuantity()))  // ✅ Convert to CartItem
                .toList();

        String transactionId = request.getTransactionId();

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (CartItem cartItem : cartItems) {
            Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Product not found: " + cartItem.getProductId());
            }

            Product product = productOpt.get();
            totalAmount += product.getPrice() * cartItem.getQuantity();

            orderItems.add(new OrderItem(product.getId(), product.getName(), product.getPrice(), cartItem.getQuantity()));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus("Processing");
        order.setTransactionId(transactionId);
        order.setOrderDate(new Date());

        orderRepository.save(order); // ✅ Save the order
        cartRepository.deleteByUserId(userId); // ✅ Clear the user's cart

        return ResponseEntity.ok(Map.of("message", "Order placed successfully!", "orderId", order.getId()));
    }

}
