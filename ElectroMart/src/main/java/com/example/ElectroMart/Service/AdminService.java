package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Role;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Repository.RoleRepository;
import com.example.ElectroMart.Repository.UserRepository;
import com.example.ElectroMart.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String loginAdmin(String email, String password) {
        User admin = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password"));

        if (!admin.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!admin.getRoles().stream().anyMatch(role -> role.equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("Access denied");
        }
        List<String> roleNames = admin.getRoles().stream()
                .map(Role::getRole) // Extract role names
                .toList();


        return jwtUtil.generateToken(admin, roleNames);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void approveUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found"));
        Role approvedRole = roleRepository.findByRole("ROLE_APPROVED_USER").orElseThrow(() ->
                new IllegalArgumentException("Role not found"));

        user.getRoles().add(approvedRole);


        userRepository.save(user);
    }

    public Object getAdminDashboard() {
        // Mock data for admin dashboard; replace with actual logic.
        return "Admin dashboard data";
    }
}
