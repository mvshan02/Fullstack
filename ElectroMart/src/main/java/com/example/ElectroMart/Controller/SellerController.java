package com.example.ElectroMart.Controller;

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

    @GetMapping("/dashboard")
    public ResponseEntity<?> getSellerDashboard(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || token.isEmpty()) {
                System.out.println("🚨 No Authorization header received!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Authorization header");
            }

            System.out.println("🔹 Received Token: " + token); // ✅ Log the token
            return ResponseEntity.ok(sellerService.getDashboard(token));
        } catch (Exception e) {
            System.out.println("❌ Error in Dashboard API: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
