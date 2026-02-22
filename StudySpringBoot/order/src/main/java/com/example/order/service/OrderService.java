package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(String id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found: " + id);
    }

    public void cancelOrder(String id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if ("PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus())) {
                order.setStatus("CANCELLED");
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Cannot cancel order in status: " + order.getStatus());
            }
        } else {
            throw new RuntimeException("Order not found: " + id);
        }
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
