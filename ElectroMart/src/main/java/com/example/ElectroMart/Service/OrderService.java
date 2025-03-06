package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Order;
import com.example.ElectroMart.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    // Place a new order
    public Order placeOrder(Order order) {
        if (order.getShippingAddress() == null) {
            throw new RuntimeException("Shipping address is missing.");
        }

        order.setStatus("Ordered"); // Default status
        return orderRepository.save(order);
    }


    // Get order by ID
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    // Get orders by user ID
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    // Update order status (e.g., "Shipped", "Delivered")
    public Order updateOrderStatus(String orderId, String newStatus) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        return null;
    }
}
