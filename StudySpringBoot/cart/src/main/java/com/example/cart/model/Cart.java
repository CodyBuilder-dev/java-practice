package com.example.cart.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String id;
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cart(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotalAmount();
    }

    public void addItem(CartItem item) {
        // 이미 있는 상품이면 수량만 증가
        for (CartItem existingItem : items) {
            if (existingItem.getProductId().equals(item.getProductId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                calculateTotalAmount();
                this.updatedAt = LocalDateTime.now();
                return;
            }
        }
        // 새로운 상품 추가
        this.items.add(item);
        calculateTotalAmount();
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        calculateTotalAmount();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateItemQuantity(String productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    removeItem(productId);
                } else {
                    item.setQuantity(quantity);
                    calculateTotalAmount();
                    this.updatedAt = LocalDateTime.now();
                }
                return;
            }
        }
    }

    public void clear() {
        this.items.clear();
        this.totalAmount = 0.0;
        this.updatedAt = LocalDateTime.now();
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
