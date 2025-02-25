package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId); // Retrieve orders by user ID
}
