package com.example.ElectroMart.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 60, message = "Password must have at least 6 characters")
    private String password;

    @NotEmpty(message = "Username is required")
    private String userName;

    @DBRef
    private Set<Role> roles = new HashSet<>();
    ; // Roles assigned to the user

    // Additional fields for all users
    private String phoneNumber;
    private String address;
    private String profileImageUrl; // URL to the profile image
    private boolean active = true; // Account status
    private LocalDateTime createdAt = LocalDateTime.now(); // Account creation timestamp
    private LocalDateTime lastLogin; // Last login timestamp

    // Seller-specific fields
//    private boolean isSeller = false; // Whether the user is a seller
    private String shopName; // Seller's shop name
    private String taxNumber; // GST number for sellers

    // Admin-specific fields
//    private boolean isAdmin = false; // Whether the user is an admin

    // Constructors
    public User() {
    }

    public User(String email, String password, String userName, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.roles = roles;
    }

   public  String getName() {
        return userName;
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

//    public boolean isSeller() {
//        return isSeller;
//    }
//
//    public void setSeller(boolean seller) {
//        isSeller = seller;
//    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

//    public boolean isAdmin() {
//        return isAdmin;
//    }
//
//    public void setAdmin(boolean admin) {
//        isAdmin = admin;
//    }

    // Helper methods
    public boolean hasRole(String roleName) {
        return roles.contains(roleName);
    }

    // Seller validation
    public void validateSellerFields() {
        if (roles.contains("ROLE_SELLER")) {
            if (shopName == null || shopName.isEmpty()) {
                throw new IllegalArgumentException("Shop name is required for sellers.");
            }
            if (taxNumber == null || taxNumber.isEmpty()) {
                throw new IllegalArgumentException("Tax number is required for sellers.");
            }
        }
    }
}