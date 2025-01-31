package com.example.ElectroMart.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "roles") // This maps the Role class to the "roles" collection in MongoDB
public class Role {

    @Id
    private String id;

    @NotBlank(message = "Role name is mandatory")
    private String role; // E.g., ROLE_USER, ROLE_ADMIN

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
//jwt fixed