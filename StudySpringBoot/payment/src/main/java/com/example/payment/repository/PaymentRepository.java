package com.example.payment.repository;

import com.example.payment.model.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    List<Payment> findAll();
    Optional<Payment> findById(UUID id);
    Payment save(Payment payment);
    Optional<Payment> update(UUID id, Payment payment);
    void deleteById(UUID id);
}

