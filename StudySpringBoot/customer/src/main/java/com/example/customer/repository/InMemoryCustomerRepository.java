package com.example.customer.repository;

import com.example.customer.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<UUID, Customer> store = new ConcurrentHashMap<>();

    public InMemoryCustomerRepository() {
        Customer c1 = new Customer(UUID.randomUUID(), "Alice", "alice@example.com");
        Customer c2 = new Customer(UUID.randomUUID(), "Bob", "bob@example.com");
        store.put(c1.getId(), c1);
        store.put(c2.getId(), c2);
    }

    @Override
    public List<Customer> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID());
        }
        store.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public Optional<Customer> update(UUID id, Customer customer) {
        if (!store.containsKey(id)) {
            return Optional.empty();
        }
        customer.setId(id);
        store.put(id, customer);
        return Optional.of(customer);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}

