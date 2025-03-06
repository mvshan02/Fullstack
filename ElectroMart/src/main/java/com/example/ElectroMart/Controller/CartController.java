package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Cart;
import com.example.ElectroMart.Model.CartItem;
import com.example.ElectroMart.Security.JwtUtil;
import com.example.ElectroMart.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    //  Get cart by user ID
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    //  Remove item from cart
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        Cart updatedCart = cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    //  Clear entire cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    //  Securely Add to Cart with JWT Token
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request,
                                       @RequestHeader("Authorization") String token) {
        try {
            String userId = (String) request.get("userId");

            if (userId == null || userId.equals("undefined")) {
                userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
                if (userId == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Error: Unauthorized user.");
                }
            }

            // 🔥 Clear previous cart data before adding new items
            cartService.clearCart(userId);

            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) request.get("items");
            if (itemsData == null || itemsData.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Cart items cannot be empty.");
            }

            List<CartItem> cartItems = new ArrayList<>();
            for (Map<String, Object> item : itemsData) {
                CartItem cartItem = new CartItem();
                cartItem.setProductId((String) item.get("productId"));
                cartItem.setQuantity(((Number) item.get("quantity")).intValue());
                cartItems.add(cartItem);
            }

            //  Save new cart with only selected items
            Cart updatedCart = cartService.addToCart(userId, cartItems);
            return ResponseEntity.ok(updatedCart);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Something went wrong - " + e.getMessage());
        }
    }
}