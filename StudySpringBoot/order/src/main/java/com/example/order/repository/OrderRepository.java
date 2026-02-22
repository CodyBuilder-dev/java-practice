package com.example.order.repository;

import com.example.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(String status);
    List<Order> findAll();
    void deleteById(String id);
}
