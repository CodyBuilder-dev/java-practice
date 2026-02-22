package com.example.product.repository;

import com.example.product.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(UUID id);
    Product save(Product product);
    Optional<Product> update(UUID id, Product product);
    void deleteById(UUID id);
}

