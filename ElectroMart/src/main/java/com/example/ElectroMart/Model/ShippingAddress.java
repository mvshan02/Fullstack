package com.example.ElectroMart.Model;

public class ShippingAddress {
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String postalCode;
    private String deliveryInstructions;

    public ShippingAddress() {}

    public ShippingAddress(String fullName, String address, String phoneNumber, String email, String postalCode, String deliveryInstructions) {
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.postalCode = postalCode;
        this.deliveryInstructions = deliveryInstructions;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
}
