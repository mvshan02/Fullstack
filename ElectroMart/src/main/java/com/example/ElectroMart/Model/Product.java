package com.example.ElectroMart.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products") // Maps to the "Products" collection in MongoDB
public class Product {
    @Id  //  Ensure this field is correctly mapped to MongoDB
    private String id;

    private String name;
    private double price;
    private String description;
    private String category;

    private int stock;
    private String imageUrl;
    private String categoryTag;
    private double rating;

    private double discountPercentage;
    private int salesCount;

    private boolean flashSaleActive;
    private boolean bigDealActive;
    private boolean topPickActive;
    private User seller;

    //  Ensure all getters/setters exist
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    // ✅ **Computed Discounted Price (Transient - Not stored in DB)**
    @Transient
    public double getDiscountedPrice() {
        return price - (price * (discountPercentage / 100));
    }

    // ✅ Getters and Setters

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }


    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    public boolean isFlashSaleActive() {
        return flashSaleActive;
    }

    public void setFlashSaleActive(boolean flashSaleActive) {
        this.flashSaleActive = flashSaleActive;
    }

    public boolean isBigDealActive() {
        return bigDealActive;
    }

    public void setBigDealActive(boolean bigDealActive) {
        this.bigDealActive = bigDealActive;
    }

    public boolean isTopPickActive() {
        return topPickActive;
    }

    public void setTopPickActive(boolean topPickActive) {
        this.topPickActive = topPickActive;
    }
    public User getSeller() { return seller; } // ✅ Get seller info
    public void setSeller(User seller) { this.seller = seller; }
}

