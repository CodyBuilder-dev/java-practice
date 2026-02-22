package com.example.product.repository;

import com.example.product.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final Map<UUID, Product> store = new ConcurrentHashMap<>();

    public InMemoryProductRepository() {
        Product p1 = new Product(UUID.randomUUID(), "Widget", "A useful widget", new java.math.BigDecimal("9.99"));
        Product p2 = new Product(UUID.randomUUID(), "Gadget", "A fancy gadget", new java.math.BigDecimal("19.99"));
        store.put(p1.getId(), p1);
        store.put(p2.getId(), p2);
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        store.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> update(UUID id, Product product) {
        if (!store.containsKey(id)) {
            return Optional.empty();
        }
        product.setId(id);
        store.put(id, product);
        return Optional.of(product);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}

