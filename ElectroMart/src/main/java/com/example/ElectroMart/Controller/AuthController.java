package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Model.Role;
import com.example.ElectroMart.Security.JwtUtil;
import com.example.ElectroMart.Service.AuthService;
import com.example.ElectroMart.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            if (authentication.isAuthenticated()) {
                List<String> roleNames = user.getRoles().stream()
                        .map(Role::getRole) // Extract role names
                        .toList();


                String token =  jwtUtil.generateToken(user.getEmail(), roleNames);
                return ResponseEntity.ok(Map.of("token", token));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        } catch (BadCredentialsException e) {
            // Log and return specific error for bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid email or password");
        } catch (Exception e) {
            // Log and return generic error for other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Something went wrong");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
//@PostMapping("/register/user")
//public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
//    authService.register(request, "ROLE_USER");
//    return ResponseEntity.ok("User registered successfully!");
//}
//
//    // Endpoint to register a SELLER
//    @PostMapping("/register/seller")
//    public ResponseEntity<String> registerSeller(@RequestBody RegisterRequest request) {
//        AuthService.register(request, "ROLE_SELLER");
//        return ResponseEntity.ok("Seller registered successfully!");
//    }
////
//    // Endpoint to register an ADMIN
//    @PostMapping("/register/admin")
//    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
//        userService.registerUser(request, "ROLE_ADMIN");
//        return ResponseEntity.ok("Admin registered successfully!");
//
//    }
}
