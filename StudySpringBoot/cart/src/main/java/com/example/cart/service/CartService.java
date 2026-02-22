package com.example.cart.service;

import com.example.cart.model.Cart;
import com.example.cart.model.CartItem;
import com.example.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getOrCreateCart(String userId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        Cart newCart = new Cart(null, userId);
        return cartRepository.save(newCart);
    }

    public Optional<Cart> getCart(String id) {
        return cartRepository.findById(id);
    }

    public Optional<Cart> getUserCart(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addItemToCart(String userId, CartItem item) {
        Cart cart = getOrCreateCart(userId);
        cart.addItem(item);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(String userId, String productId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.removeItem(productId);
            return cartRepository.save(cart);
        }
        throw new RuntimeException("Cart not found for user: " + userId);
    }

    public Cart updateItemQuantity(String userId, String productId, int quantity) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.updateItemQuantity(productId, quantity);
            return cartRepository.save(cart);
        }
        throw new RuntimeException("Cart not found for user: " + userId);
    }

    public Cart clearCart(String userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.clear();
            return cartRepository.save(cart);
        }
        throw new RuntimeException("Cart not found for user: " + userId);
    }

    public void deleteCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
