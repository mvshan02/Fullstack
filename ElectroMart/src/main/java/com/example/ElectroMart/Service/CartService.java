package com.example.ElectroMart.Service;

import com.example.ElectroMart.Model.Cart;
import com.example.ElectroMart.Model.CartItem;
import com.example.ElectroMart.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // ✅ Add multiple items to the cart (Create or Update)
    public Cart addToCart(String userId, List<CartItem> newItems) {
        Optional<Cart> existingCartOpt = cartRepository.findByUserId(userId);
        Cart cart;

        if (existingCartOpt.isPresent()) {
            cart = existingCartOpt.get();
            cart.setItems(newItems); //  Instead of appending, we REPLACE the cart items
        } else {
            cart = new Cart(userId, newItems);
        }

        return cartRepository.save(cart);
    }



    //  Get Cart by User ID
    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> new Cart(userId, new ArrayList<>()));
    }

    //  Remove an item from the cart
    public Cart removeFromCart(String userId, String productId) {
        Optional<Cart> existingCartOpt = cartRepository.findByUserId(userId);

        if (existingCartOpt.isPresent()) {
            Cart cart = existingCartOpt.get();
            List<CartItem> items = cart.getItems();

            items.removeIf(item -> item.getProductId().equals(productId)); //  Remove item

            cart.setItems(items);
            return cartRepository.save(cart);
        }
        return null;
    }

    //  Clear the entire cart for a user
    public void clearCart(String userId) {
        cartRepository.findByUserId(userId).ifPresent(cartRepository::delete);
    }
}
