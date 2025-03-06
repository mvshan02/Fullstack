package com.example.ElectroMart.Model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CartItem {
    private String productId;
    private int quantity;
    private double price;

    public CartItem() {}

    public CartItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {  // ✅ Add this method
        return price;
    }

    public void setPrice(double price) {  // ✅ Add this method
        this.price = price;
    }
    @Override
    public String toString() {
        return "Product ID: " + productId + ", Quantity: " + quantity + ", Price: $" + price;
    }
}
