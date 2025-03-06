package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId); // ✅ Finds cart by user ID
    void deleteByUserId(String userId);
}
