package com.example.customer.repository;

import com.example.customer.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    List<Customer> findAll();
    Optional<Customer> findById(UUID id);
    Customer save(Customer customer);
    Optional<Customer> update(UUID id, Customer customer);
    void deleteById(UUID id);
}

