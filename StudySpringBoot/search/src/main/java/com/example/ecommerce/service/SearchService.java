package com.example.ecommerce.service;

import com.example.ecommerce.model.ProductDocument;
import com.example.ecommerce.repository.SearchRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SearchService {
    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public Flux<ProductDocument> searchProducts(String keyword) {
        return searchRepository.searchByKeyword(keyword);
    }

    public Flux<ProductDocument> searchByCategory(String category) {
        return searchRepository.searchByCategory(category);
    }

    public Flux<ProductDocument> searchByPriceRange(Double minPrice, Double maxPrice) {
        return searchRepository.searchByPriceRange(minPrice, maxPrice);
    }

    public Mono<ProductDocument> indexProduct(ProductDocument product) {
        return searchRepository.indexProduct(product);
    }

    public Mono<Void> deleteProduct(String id) {
        return searchRepository.deleteProduct(id);
    }
}
