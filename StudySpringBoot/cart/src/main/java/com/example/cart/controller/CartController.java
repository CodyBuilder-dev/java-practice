package com.example.cart.controller;

import com.example.cart.model.Cart;
import com.example.cart.model.CartItem;
import com.example.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getUserCart(@PathVariable String userId) {
        Optional<Cart> cart = cartService.getUserCart(userId);
        return cart.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(cartService.getOrCreateCart(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable String id) {
        Optional<Cart> cart = cartService.getCart(id);
        return cart.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<Cart> addItem(
            @PathVariable String userId,
            @RequestBody CartItem item) {
        Cart cart = cartService.addItemToCart(userId, item);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable String userId,
            @PathVariable String productId) {
        try {
            Cart cart = cartService.removeItemFromCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable String userId,
            @PathVariable String productId,
            @RequestBody QuantityUpdateRequest request) {
        try {
            Cart cart = cartService.updateItemQuantity(userId, productId, request.getQuantity());
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    static class QuantityUpdateRequest {
        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
