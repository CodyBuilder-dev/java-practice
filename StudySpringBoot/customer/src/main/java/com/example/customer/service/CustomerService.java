package com.example.customer.service;

import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> list() {
        return repository.findAll();
    }

    public Optional<Customer> get(UUID id) {
        return repository.findById(id);
    }

    public Customer create(Customer customer) {
        return repository.save(customer);
    }

    public Optional<Customer> update(UUID id, Customer customer) {
        return repository.update(id, customer);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}

