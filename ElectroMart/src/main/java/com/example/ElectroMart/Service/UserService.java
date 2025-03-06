package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Role;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Repository.RoleRepository;
import com.example.ElectroMart.Repository.UserRepository;
import com.example.ElectroMart.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;




@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User getUserFromToken(String token) {
        // ✅ Decode token to extract email
        String email = jwtUtil.extractEmail(token);

        // ✅ Find user by email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    @PostMapping("/api/auth/register")
    public ResponseEntity<?> registerUser(User user, String roleName) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        // ✅ Ensure userName is provided
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign the specified role (User or Seller)
        Role role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException("Role not Found: " + roleName));
        user.setRoles(Set.of(role));

        userRepository.save(user);
        return ResponseEntity.ok(roleName + " registered successfully");
    }

//    public ResponseEntity<?> registerUser(@RequestBody User user) {
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("Email is already in use");
//        }
//
//
//        // Encrypt the password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // Check if roles are provided; if not, assign ROLE_USER by default
//        if (user.getRoles() == null || user.getRoles().isEmpty()) {
//            Role defaultRole = roleRepository.findByRole("ROLE_USER")
//                    .orElseThrow(() -> new RuntimeException("Default role not found"));
//            user.setRoles(Set.of(defaultRole));
//        } else {
//            // Map provided role names to Role objects
//            Set<Role> assignedRoles = new HashSet<>();
//            for (Role role : user.getRoles()) {
//                Role dbRole = roleRepository.findByRole(role.getRole())
//                        .orElseThrow(() -> new RuntimeException("Role not found: " + role.getRole()));
//                assignedRoles.add(dbRole);
//            }
//            user.setRoles(assignedRoles);
//        }
//
//        userRepository.save(user);
//        return ResponseEntity.ok("User registered successfully");
//
//
//    }



    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRole) // Extract role names
                .toList();


        return jwtUtil.generateToken(user, roleNames);
    }

    public User getUserProfile(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // Update User Profile
//    public User updateUserProfile(User updatedUser) {
//        String currentUserEmail = jwtUtil.extractUsernameFromSecurityContext();
//        User existingUser = userRepository.findByEmail(currentUserEmail)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        existingUser.setName(updatedUser.getName());
//        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
//            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        }
//        return userRepository.save(existingUser);
//    }
    public User updateUserProfile(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword()); // Ensure password is hashed if using authentication
        return userRepository.save(existingUser);
    }


}
