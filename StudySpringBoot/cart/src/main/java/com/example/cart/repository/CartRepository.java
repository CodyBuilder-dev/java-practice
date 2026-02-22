package com.example.cart.repository;

import com.example.cart.model.Cart;

import java.util.Optional;

public interface CartRepository {
    Cart save(Cart cart);
    Optional<Cart> findById(String id);
    Optional<Cart> findByUserId(String userId);
    void deleteById(String id);
    void deleteByUserId(String userId);
}
