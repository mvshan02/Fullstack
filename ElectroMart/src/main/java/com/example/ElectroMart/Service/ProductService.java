package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Add a new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }


    // Get all products
    // ✅ Fetch all products including discount price
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();

        // Compute Discounted Price before returning
        return products.stream().map(product -> {
            product.setPrice(product.getDiscountedPrice());
            return product;
        }).collect(Collectors.toList());
    }


    // Get a product by ID
    public Optional<Product> getProductById(String id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            System.err.println(" Error fetching product: " + e.getMessage());
            return Optional.empty();
        }
    }


    // Update an existing product
    // ✅ Updated Product Update Method
    public Product updateProduct(String id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDiscountPercentage(updatedProduct.getDiscountPercentage());
            existingProduct.setRating(updatedProduct.getRating());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());

            // ✅ Ensure Flash Sale, Big Deal, and Top Picks are updated
            existingProduct.setFlashSaleActive(updatedProduct.isFlashSaleActive());
            existingProduct.setBigDealActive(updatedProduct.isBigDealActive());
            existingProduct.setTopPickActive(updatedProduct.isTopPickActive());

            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("⚠️ Product not found!"));
    }

    // Delete a product by ID
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException(" Cannot delete: Product with ID " + id + " not found!");
        }
        productRepository.deleteById(id);
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category.toLowerCase());

        if (products.isEmpty()) {
            throw new RuntimeException(" No products found in category: " + category);
        }

        //  Ensure each product has an _id
        for (Product product : products) {
            if (product.getId() == null || product.getId().isEmpty()) {
                System.out.println(" Product is missing an _id: " + product.getName());

                //  Fix by assigning a new unique ID (if necessary)
                product.setId(new org.bson.types.ObjectId().toHexString());
                productRepository.save(product);  // Save the updated product with the ID
            }
        }

        return products;
    }

    // Get all products with sorting
    public List<Product> getAllProducts(String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        return productRepository.findAll(sort);
    }

    // ✅ Search by product name (Case-insensitive)
    public List<Product> searchProducts(String name, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order.toUpperCase()), sortBy);
        return productRepository.findByNameRegex(name, sort);
    }

    // ✅ Corrected: Filter products by price range with sorting
    public List<Product> filterByPriceRange(double minPrice, double maxPrice, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order.toUpperCase()), sortBy);
        return productRepository.findByPriceBetween(minPrice, maxPrice, sort);
    }

    // ✅ Combined Search, Filter, and Sorting
    public List<Product> searchFilterSort(String name, String category, Double minPrice, Double maxPrice, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.fromString(order.toUpperCase()), sortBy);

        if (name != null && !name.isEmpty()) {
            return productRepository.findByNameRegex(name, sort);
        } else if (category != null && !category.isEmpty()) {
            return productRepository.findByCategoryIgnoreCase(category, sort);
        } else if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice, sort);
        } else {
            return productRepository.findAll(sort);
        }
    }
    public List<String> getSearchSuggestions(String query) {
        return getSearchSuggestions(query, "name", "asc"); // Default sorting by name (ascending)
    }

    public List<String> getSearchSuggestions(String query, String sortBy, String order) {
        // Define sorting order
        Sort sort = Sort.by(Sort.Direction.fromString(order.toUpperCase()), sortBy);

        // Fetch products based on case-insensitive regex search and sorting
        List<Product> products = productRepository.findByNameRegex(query, sort);

        // Extract product names for suggestions
        return products.stream().map(Product::getName).collect(Collectors.toList());
    }

    public List<Product> getFlashSaleProducts() {
        return productRepository.findByFlashSaleActiveTrue();
    }

    public List<Product> getBigDealProducts() {
        return productRepository.findByBigDealActiveTrue();
    }

    public List<Product> getTopPickProducts() {
        return productRepository.findByTopPickActiveTrue();
    }

}



