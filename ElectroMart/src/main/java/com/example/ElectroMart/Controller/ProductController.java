package com.example.ElectroMart.Controller;
import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Service.ProductService;
import com.example.ElectroMart.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService; //  Needed to extract seller info from the token

    //  Secure Product Creation (Requires Authentication)
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("⚠️ No valid token provided.");
            }

            //  Extract user from token
            User seller = userService.getUserFromToken(token.replace("Bearer ", ""));
            if (seller == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("⚠️ Invalid user.");
            }

            product.setSeller(seller); //  Assign seller to product
            Product savedProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error: " + e.getMessage());
        }

    }


    // Get all products
    @GetMapping("/product")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" No products found.");
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching products: " + e.getMessage());
        }
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProduct = productService.getProductById(id);

        if (!existingProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("⚠️ Product not found!");
        }

        Product product = existingProduct.get();
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDiscountPercentage(updatedProduct.getDiscountPercentage());
        product.setRating(updatedProduct.getRating());
        product.setDescription(updatedProduct.getDescription());
        product.setCategory(updatedProduct.getCategory());
        product.setStock(updatedProduct.getStock());
        product.setImageUrl(updatedProduct.getImageUrl());

        Product savedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(savedProduct);
    }


    // Delete a product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    //  Fix: Allow public category-based fetching
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return products.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found in this category.")
                : ResponseEntity.ok(products);
    }
    // Unified Search, Filter, and Sorting Endpoint
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        List<Product> products = productService.searchFilterSort(name, category, minPrice, maxPrice, sortBy, order);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSearchSuggestions(@RequestParam("query") String query) {
        List<String> suggestions = productService.getSearchSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
    @GetMapping("/flash-sale")
    public ResponseEntity<List<Product>> getFlashSaleProducts() {
        List<Product> flashSales = productService.getFlashSaleProducts();
        return flashSales.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(flashSales);
    }
    @GetMapping("/big-deals")
    public ResponseEntity<List<Product>> getBigDealProducts() {
        List<Product> bigDeals = productService.getBigDealProducts();
        return bigDeals.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bigDeals);
    }

    @GetMapping("/top-picks")
    public ResponseEntity<List<Product>> getTopPickProducts() {
        List<Product> topPicks = productService.getTopPickProducts();
        return topPicks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(topPicks);
    }

}