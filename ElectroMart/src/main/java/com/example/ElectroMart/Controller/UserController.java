package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Service.UserService;
import com.example.ElectroMart.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;  // Inject JWT utility class for token decoding

    // Get User Profile
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently authenticated");
        }

        try {
            // Remove "Bearer " prefix from token
            String jwt = token.replace("Bearer ", "");

            // Extract userId from JWT token
            String userId = jwtUtil.extractUserId(jwt);

            // Find user from database
            User user = userService.getUserProfile(userId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // Update User Profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedUser) {
        try {
            User user = userService.updateUserProfile(updatedUser);
            return ResponseEntity.ok().body("Profile updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
