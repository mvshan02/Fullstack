package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Order;
import com.example.ElectroMart.Model.Product;
import com.example.ElectroMart.Model.User;
import com.example.ElectroMart.Repository.OrderRepository;
import com.example.ElectroMart.Repository.ProductRepository;
import com.example.ElectroMart.Repository.UserRepository;
import com.example.ElectroMart.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class SellerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> getDashboard(String token) {
        // 🛠️ Extract seller email from JWT token
        String sellerEmail = jwtUtil.extractEmail(token);

        // 🔹 Validate seller exists
        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        // 🔹 Fetch seller's products & orders
        List<Product> sellerProducts = productRepository.findBySeller(seller);
        List<Order> sellerOrders = orderRepository.findBySeller(seller);

        // 🔹 Calculate sales and order stats
        double totalSales = sellerOrders.stream().mapToDouble(Order::getTotalAmount).sum();
        long completedOrders = sellerOrders.stream().filter(order -> "Completed".equalsIgnoreCase(order.getStatus())).count();
        long pendingOrders = sellerOrders.stream().filter(order -> "Pending".equalsIgnoreCase(order.getStatus())).count();

        // 🔹 Construct response
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("sellerName", seller.getUserName());
        dashboardData.put("totalProducts", sellerProducts.size());
        dashboardData.put("totalSales", totalSales);
        dashboardData.put("pendingOrders", pendingOrders);
        dashboardData.put("completedOrders", completedOrders);
        dashboardData.put("recentOrders", sellerOrders);
        dashboardData.put("products", sellerProducts);

        return dashboardData;
    }
}
