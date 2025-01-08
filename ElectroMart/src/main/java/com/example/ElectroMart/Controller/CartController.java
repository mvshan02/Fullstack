package com.example.ElectroMart.Controller;

import com.example.ElectroMart.Model.Cart;
import com.example.ElectroMart.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public Cart addCart(@RequestBody Cart cart) {
        return cartService.addCart(cart);
    }

    @GetMapping("/user/{userId}")
    public List<Cart> getCartsByUserId(@PathVariable String userId) {
        return cartService.getCartsByUserId(userId);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable String cartId) {
        cartService.deleteCart(cartId);
    }
}
