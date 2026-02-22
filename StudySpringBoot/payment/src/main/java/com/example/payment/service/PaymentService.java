package com.example.payment.service;

import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<Payment> list() {
        return repository.findAll();
    }

    public Optional<Payment> get(UUID id) {
        return repository.findById(id);
    }

    public Payment create(Payment payment) {
        return repository.save(payment);
    }

    public Optional<Payment> update(UUID id, Payment payment) {
        return repository.update(id, payment);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
