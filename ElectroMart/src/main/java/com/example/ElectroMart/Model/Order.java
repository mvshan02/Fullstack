package com.example.ElectroMart.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")  // Storing orders in MongoDB
public class Order {
    @Id
    private String id;

    private String userId;

    @DBRef
    private User seller;

    @DBRef
    private User buyer;

    private List<OrderItem> items;
    private double totalAmount;
    private String paymentMethod;
    private String status; // Ordered, Shipped, Out for Delivery, Delivered
    private ShippingAddress shippingAddress;
    private String transactionId;
    private Date orderDate;





    // Constructors
    public Order() {}

    public Order(String userId, User buyer, User seller, List<OrderItem> items, double totalAmount,
                 ShippingAddress shippingAddress, String paymentMethod, String status, String transactionId) {
        this.userId = userId;
        this.buyer = buyer;
        this.seller = seller;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.transactionId = transactionId;
    }


    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }

    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
