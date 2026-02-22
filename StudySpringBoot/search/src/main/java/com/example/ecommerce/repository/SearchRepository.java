package com.example.ecommerce.repository;

import com.example.ecommerce.model.ProductDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchRepository {
    Flux<ProductDocument> searchByKeyword(String keyword);
    Flux<ProductDocument> searchByCategory(String category);
    Flux<ProductDocument> searchByPriceRange(Double minPrice, Double maxPrice);
    Mono<ProductDocument> indexProduct(ProductDocument product);
    Mono<Void> deleteProduct(String id);
}
