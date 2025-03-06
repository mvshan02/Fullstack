package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Order;
import com.example.ElectroMart.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findBySeller(User seller);// Retrieve orders by user ID
}
