package com.example.ecommerce.repository;

import com.example.ecommerce.model.Notification;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryNotificationRepository implements NotificationRepository {
    private final Map<String, Notification> notifications = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Mono<Notification> save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(String.valueOf(idGenerator.getAndIncrement()));
        }
        notifications.put(notification.getId(), notification);
        return Mono.just(notification);
    }

    @Override
    public Mono<Notification> findById(String id) {
        return Mono.justOrEmpty(notifications.get(id));
    }

    @Override
    public Flux<Notification> findByUserId(String userId) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> notification.getUserId().equals(userId));
    }

    @Override
    public Flux<Notification> findByStatus(String status) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> notification.getStatus().equals(status));
    }

    @Override
    public Flux<Notification> findAll() {
        return Flux.fromIterable(notifications.values());
    }
}
