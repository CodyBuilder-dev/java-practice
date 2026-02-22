package com.example.ecommerce.controller;

import com.example.ecommerce.model.ProductDocument;
import com.example.ecommerce.service.SearchService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public Flux<ProductDocument> searchProducts(@RequestParam String keyword) {
        return searchService.searchProducts(keyword);
    }

    @GetMapping("/category/{category}")
    public Flux<ProductDocument> searchByCategory(@PathVariable String category) {
        return searchService.searchByCategory(category);
    }

    @GetMapping("/price")
    public Flux<ProductDocument> searchByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return searchService.searchByPriceRange(minPrice, maxPrice);
    }

    @PostMapping("/index")
    public Mono<ProductDocument> indexProduct(@RequestBody ProductDocument product) {
        return searchService.indexProduct(product);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return searchService.deleteProduct(id);
    }
}
