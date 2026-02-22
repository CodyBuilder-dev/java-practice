package com.example.payment.repository;

import com.example.payment.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository {
    private final Map<UUID, Payment> store = new ConcurrentHashMap<>();

    public InMemoryPaymentRepository() {
        Payment p1 = new Payment(UUID.randomUUID(), UUID.randomUUID(), new java.math.BigDecimal("100.00"), "COMPLETED");
        Payment p2 = new Payment(UUID.randomUUID(), UUID.randomUUID(), new java.math.BigDecimal("50.00"), "PENDING");
        store.put(p1.getId(), p1);
        store.put(p2.getId(), p2);
    }

    @Override
    public List<Payment> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(UUID.randomUUID());
        }
        store.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> update(UUID id, Payment payment) {
        if (!store.containsKey(id)) {
            return Optional.empty();
        }
        payment.setId(id);
        store.put(id, payment);
        return Optional.of(payment);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}

