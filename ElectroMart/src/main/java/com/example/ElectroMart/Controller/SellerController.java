package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerSeller(@RequestBody User seller) {
        try {
            sellerService.registerSeller(seller);
            return ResponseEntity.status(HttpStatus.CREATED).body("Seller registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginSeller(@RequestParam String email, @RequestParam String password) {
        try {
            String token = sellerService.loginSeller(email, password);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            sellerService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getSellerDashboard() {
        try {
            return ResponseEntity.ok(sellerService.getDashboard());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
