package com.example.ElectroMart.Model;

import java.util.List;

public class PaymentSuccessRequest {
    private String userId;
    private String transactionId;
    private List<PaymentRequest.CartItem> cartItems;  // Products paid for

    // Constructors
    public PaymentSuccessRequest() {}

    public PaymentSuccessRequest(String userId, String transactionId, List<PaymentRequest.CartItem> cartItems) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.cartItems = cartItems;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<PaymentRequest.CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<PaymentRequest.CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
