package com.example.ecommerce.repository;

import com.example.ecommerce.model.ProductDocument;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySearchRepository implements SearchRepository {
    private final Map<String, ProductDocument> products = new ConcurrentHashMap<>();

    public InMemorySearchRepository() {
        // 초기 테스트 데이터
        products.put("1", new ProductDocument("1", "MacBook Pro", "Apple 노트북", 2500000.0, "Electronics", 10));
        products.put("2", new ProductDocument("2", "iPhone 15", "Apple 스마트폰", 1200000.0, "Electronics", 50));
        products.put("3", new ProductDocument("3", "Nike Air Max", "나이키 운동화", 150000.0, "Shoes", 30));
        products.put("4", new ProductDocument("4", "Samsung TV", "삼성 스마트 TV", 1800000.0, "Electronics", 5));
        products.put("5", new ProductDocument("5", "Adidas Jacket", "아디다스 재킷", 80000.0, "Clothing", 20));
    }

    @Override
    public Flux<ProductDocument> searchByKeyword(String keyword) {
        return Flux.fromIterable(products.values())
                .filter(product ->
                    product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(keyword.toLowerCase())
                );
    }

    @Override
    public Flux<ProductDocument> searchByCategory(String category) {
        return Flux.fromIterable(products.values())
                .filter(product -> product.getCategory().equalsIgnoreCase(category));
    }

    @Override
    public Flux<ProductDocument> searchByPriceRange(Double minPrice, Double maxPrice) {
        return Flux.fromIterable(products.values())
                .filter(product ->
                    product.getPrice() >= minPrice && product.getPrice() <= maxPrice
                );
    }

    @Override
    public Mono<ProductDocument> indexProduct(ProductDocument product) {
        products.put(product.getId(), product);
        return Mono.just(product);
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        products.remove(id);
        return Mono.empty();
    }
}
