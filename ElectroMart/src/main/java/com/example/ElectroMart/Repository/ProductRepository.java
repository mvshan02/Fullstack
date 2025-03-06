package com.example.ElectroMart.Repository;

import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Model.User;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Repository // Optional but can be added for clarity
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByName(String name); // Custom query (optional)

    List<Product> findBySeller(User seller);
    List<Product> findByCategory(String category);

    // ✅ Add this for filtering by categoryTag
    List<Product> findByCategoryTag(String categoryTag);

    // ✅ Handling MongoDB "_id" issues
    Optional<Product> findById(ObjectId id);

    // Search by product name (case insensitive)
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Product> findByNameRegex(String name, Sort sort);

    // Filter by category
    List<Product> findByCategoryIgnoreCase(String category, Sort sort);

    // Filter by price range
    List<Product> findByPriceBetween(double minPrice, double maxPrice, Sort sort);

    // Sort products dynamically (handled in ProductService)
    List<Product> findAll(Sort sort);

    List<Product> findByFlashSaleActiveTrue();

    List<Product> findByBigDealActiveTrue();


    List<Product> findByTopPickActiveTrue();
}

