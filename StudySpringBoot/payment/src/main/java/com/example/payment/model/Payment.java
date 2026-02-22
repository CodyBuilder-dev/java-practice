package com.example.payment.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Payment {
    private UUID id;
    private UUID orderId;
    private BigDecimal amount;
    private String status;

    public Payment() {}

    public Payment(UUID id, UUID orderId, BigDecimal amount, String status) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

