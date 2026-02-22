package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> list() {
        return repository.findAll();
    }

    public Optional<Product> get(UUID id) {
        return repository.findById(id);
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Optional<Product> update(UUID id, Product product) {
        return repository.update(id, product);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}

