package com.example.customer.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 고객 주문 통계 DTO
 * - Order Service로부터 받아올 데이터 구조
 */
public class CustomerOrderStats {
    private UUID customerId;
    private BigDecimal totalAmount;
    private Integer orderCount;
    private Integer completedOrderCount;

    public CustomerOrderStats() {
        this.totalAmount = BigDecimal.ZERO;
        this.orderCount = 0;
        this.completedOrderCount = 0;
    }

    public CustomerOrderStats(UUID customerId, BigDecimal totalAmount, Integer orderCount) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderCount = orderCount;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getCompletedOrderCount() {
        return completedOrderCount;
    }

    public void setCompletedOrderCount(Integer completedOrderCount) {
        this.completedOrderCount = completedOrderCount;
    }
}
