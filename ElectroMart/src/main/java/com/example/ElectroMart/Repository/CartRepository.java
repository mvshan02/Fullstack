package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByUserId(String userId); // Custom query to find carts by user ID
}
