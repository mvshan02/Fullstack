package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Order;
import com.example.ElectroMart.Service.OrderService;
import com.example.ElectroMart.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService; // Needed to extract user info from token

    //  Place a new order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Order order,
                                        @RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: No valid token.");
            }

            //  Extract user from token
            String userId = userService.getUserFromToken(token.replace("Bearer ", "")).getId();
            order.setUserId(userId); // Assign user ID

            // Ensure order has valid items
            if (order.getItems() == null || order.getItems().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" Order must contain at least one item.");
            }

            Order savedOrder = orderService.placeOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder); //  Return created order
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" Error placing order: " + e.getMessage());
        }
    }


    //  Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    //  Get all orders by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    //  Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody String newStatus) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return updatedOrder != null
                ? ResponseEntity.ok(updatedOrder)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
    }
}
