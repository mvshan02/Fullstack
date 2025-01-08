package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository // Optional but can be added for clarity
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByName(String name); // Custom query (optional)
}
