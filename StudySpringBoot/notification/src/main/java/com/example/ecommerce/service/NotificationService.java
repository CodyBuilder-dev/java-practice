package com.example.ecommerce.service;

import com.example.ecommerce.model.Notification;
import com.example.ecommerce.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Mono<Notification> sendNotification(Notification notification) {
        return notificationRepository.save(notification)
                .delayElement(Duration.ofMillis(100)) // 전송 시뮬레이션
                .flatMap(saved -> {
                    saved.setStatus("SENT");
                    saved.setSentAt(LocalDateTime.now());
                    return notificationRepository.save(saved);
                });
    }

    public Mono<Notification> getNotification(String id) {
        return notificationRepository.findById(id);
    }

    public Flux<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Flux<Notification> getPendingNotifications() {
        return notificationRepository.findByStatus("PENDING");
    }

    public Flux<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Flux<Notification> sendBulkNotifications(Flux<Notification> notifications) {
        return notifications
                .flatMap(this::sendNotification)
                .onErrorContinue((error, obj) -> {
                    System.err.println("Failed to send notification: " + error.getMessage());
                });
    }
}
