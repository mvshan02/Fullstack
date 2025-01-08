package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Tracking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackingRepository extends MongoRepository<Tracking, String> {
    Tracking findByOrderId(String orderId);
}
