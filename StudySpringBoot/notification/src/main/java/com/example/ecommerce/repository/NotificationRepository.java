package com.example.ecommerce.repository;

import com.example.ecommerce.model.Notification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationRepository {
    Mono<Notification> save(Notification notification);
    Mono<Notification> findById(String id);
    Flux<Notification> findByUserId(String userId);
    Flux<Notification> findByStatus(String status);
    Flux<Notification> findAll();
}
