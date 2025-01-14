package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Model.Role;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Repository.ProductRepository;
import com.example.ElectroMart.Repository.RoleRepository;
import com.example.ElectroMart.Repository.UserRepository;
import com.example.ElectroMart.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SellerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerSeller(User seller) {
        if (userRepository.findByEmail(seller.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }

        Role sellerRole = roleRepository.findByRole("ROLE_SELLER").orElseThrow(() ->
                new IllegalArgumentException("Role not found"));
        seller.setRoles(Set.of(sellerRole));
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));

        userRepository.save(seller);
    }

    public String loginSeller(String email, String password) {
        User seller = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, seller.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        List<String> roleNames = seller.getRoles().stream()
                .map(Role::getRole) // Extract role names
                .toList();



        return jwtUtil.generateToken(seller.getEmail(), roleNames);
    }

    public void addProduct(Product product) {
        String currentSellerEmail = jwtUtil.extractUsernameFromSecurityContext();
        User seller = userRepository.findByEmail(currentSellerEmail).orElseThrow(() ->
                new IllegalArgumentException("Seller not found"));

        product.setSeller(seller);
        productRepository.save(product);
    }

    public Object getDashboard() {
        String currentSellerEmail = jwtUtil.extractUsernameFromSecurityContext();
        User seller = userRepository.findByEmail(currentSellerEmail).orElseThrow(() ->
                new IllegalArgumentException("Seller not found"));

        return productRepository.findById(seller.getId());
    }
}
