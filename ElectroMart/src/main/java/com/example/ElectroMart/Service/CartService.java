package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Cart;
import com.example.ElectroMart.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public List<Cart> getCartsByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void deleteCart(String cartId) {
        cartRepository.deleteById(cartId);
    }
}
