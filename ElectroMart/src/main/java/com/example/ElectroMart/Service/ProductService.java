package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Add a new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }


    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
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
    public Product updateProduct(String id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory()); //  Added category update
            existingProduct.setStock(updatedProduct.getStock()); //  Added stock update
            existingProduct.setImageUrl(updatedProduct.getImageUrl()); // Added image update


            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException(" Product not found!"));
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



}

