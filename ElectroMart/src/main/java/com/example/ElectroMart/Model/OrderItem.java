package com.example.ElectroMart.Model;

public class OrderItem {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String imageUrl;

    // Constructors
    public OrderItem() {}

    public OrderItem(String productId, String name, int quantity, double price, String imageUrl) {
        this.productId = productId;
        this.productName = name;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    public OrderItem(String productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }


    // Getters & Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return productName;
    }

    public void setName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
