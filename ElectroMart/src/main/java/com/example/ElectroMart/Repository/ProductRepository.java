package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Optional but can be added for clarity
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByName(String name); // Custom query (optional)
    List<Product> findByCategory(String category);

    // ✅ Add this for filtering by categoryTag
    List<Product> findByCategoryTag(String categoryTag);

    // ✅ Handling MongoDB "_id" issues
    Optional<Product> findById(ObjectId id);
}
