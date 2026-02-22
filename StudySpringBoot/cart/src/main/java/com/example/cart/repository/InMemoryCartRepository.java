package com.example.cart.repository;

import com.example.cart.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCartRepository implements CartRepository {
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();
    private final Map<String, String> userIdToCartId = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Cart save(Cart cart) {
        if (cart.getId() == null) {
            cart.setId(String.valueOf(idGenerator.getAndIncrement()));
        }
        carts.put(cart.getId(), cart);
        userIdToCartId.put(cart.getUserId(), cart.getId());
        return cart;
    }

    @Override
    public Optional<Cart> findById(String id) {
        return Optional.ofNullable(carts.get(id));
    }

    @Override
    public Optional<Cart> findByUserId(String userId) {
        String cartId = userIdToCartId.get(userId);
        if (cartId != null) {
            return Optional.ofNullable(carts.get(cartId));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {
        Cart cart = carts.remove(id);
        if (cart != null) {
            userIdToCartId.remove(cart.getUserId());
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        String cartId = userIdToCartId.remove(userId);
        if (cartId != null) {
            carts.remove(cartId);
        }
    }
}
