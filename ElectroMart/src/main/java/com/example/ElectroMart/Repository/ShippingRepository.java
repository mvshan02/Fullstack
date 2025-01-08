package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Shipping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ShippingRepository extends MongoRepository<Shipping, String> {
    List<Shipping> findByUserId(String userId); // Custom query to find shipping records by user ID
}
