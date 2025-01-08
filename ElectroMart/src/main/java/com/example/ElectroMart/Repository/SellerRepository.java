package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepository extends MongoRepository<Seller, String> {
    Seller findByEmail(String email); // Custom query to find a seller by email
}
