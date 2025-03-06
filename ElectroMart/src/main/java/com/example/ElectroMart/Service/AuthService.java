package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Role;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Repository.RoleRepository;
import com.example.ElectroMart.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public void register(RegisterRequest request, String roleName) {
//        // Fetch or create the role
//        Role role = roleRepository.findByName(roleName)
//                .orElseGet(() -> roleRepository.save(new Role(roleName)));
//
//        // Create a new user with the role
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setEmail(request.getEmail());
//        user.setRoles(List.of(role)); // Assign the role
//
//        userRepository.save(user);
//    }
}
