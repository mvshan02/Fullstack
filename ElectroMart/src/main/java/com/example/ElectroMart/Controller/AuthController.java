package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Role;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Security.JwtUtil;
import com.example.ElectroMart.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;
    @RequestMapping(value = "/api/auth/login", method = RequestMethod.GET)
    public ResponseEntity<String> handleInvalidLoginMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Error: Only POST method is allowed for login.");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {

        try {
            // Extract login details
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            String requestedRole = loginRequest.get("requestedRole"); // Fix: Extract requested role

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );


            if (authentication.isAuthenticated()) {
                User dbUser = userService.getUserByEmail(email);
                List<String> roleNames = dbUser.getRoles().stream()
                        .map(Role::getRole) // Extract role names
                        .toList();

                // Validate role-based login attempt
                if ("seller".equalsIgnoreCase(requestedRole) && !roleNames.contains("ROLE_SELLER")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Error: You are not registered as a seller.");
                }

                if ("buyer".equalsIgnoreCase(requestedRole) && !roleNames.contains("ROLE_BUYER")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Error: You are not registered as a buyer.");
                }



                String token =  jwtUtil.generateToken(dbUser.getEmail(), roleNames);

                System.out.println("✅ User Authenticated: " + email);
                System.out.println("✅ Assigned Roles: " + roleNames);
                System.out.println("✅ Generated Token: " + token);

                return ResponseEntity.ok(Map.of("token", token, "role", roleNames));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");


        } catch (BadCredentialsException e) {
            // Log and return specific error for bad credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid Email or password");
        } catch (Exception e) {
            // Log and return generic error for other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Something went wrong");
        }
    }


//    @PostMapping("/register")
//    public ResponseEntity<String> register(@Valid @RequestBody User user) {
//        System.out.println("Received user data: " + user.toString());
//
//        try {
//            userService.registerUser(user);
//            return ResponseEntity.ok("User registered successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return userService.registerUser(user, "ROLE_BUYER");
    }

    @PostMapping("/register/seller")
    public ResponseEntity<?> registerSeller(@RequestBody User user) {
        return userService.registerUser(user, "ROLE_SELLER");
    }
////
//    // Endpoint to register an ADMIN
//    @PostMapping("/register/admin")
//    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
//        userService.registerUser(request, "ROLE_ADMIN");
//        return ResponseEntity.ok("Admin registered successfully!");
//
//    }
}
