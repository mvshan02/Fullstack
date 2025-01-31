package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;


    // ✅ Admin Login
    @PostMapping("/login")


    public ResponseEntity<?> loginAdmin(@RequestParam String email, @RequestParam String password) {
        try {
            String token = adminService.loginAdmin(email, password);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    // ✅ Get Admin Dashboard



    public ResponseEntity<?> getAdminDashboard() {
        try {
            return ResponseEntity.ok(adminService.getAdminDashboard());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // ✅ Get All Users
    @GetMapping("/users")


    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(adminService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // ✅ Approve User
    @PutMapping("/users/approve/{id}")

    public ResponseEntity<?> approveUser(@PathVariable String id) {
        try {
            adminService.approveUser(id);
            return ResponseEntity.ok("User approved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
