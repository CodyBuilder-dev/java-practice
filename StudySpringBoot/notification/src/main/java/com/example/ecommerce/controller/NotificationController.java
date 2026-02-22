package com.example.ecommerce.controller;

import com.example.ecommerce.model.Notification;
import com.example.ecommerce.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Mono<Notification> sendNotification(@RequestBody Notification notification) {
        return notificationService.sendNotification(notification);
    }

    @PostMapping("/bulk")
    public Flux<Notification> sendBulkNotifications(@RequestBody Flux<Notification> notifications) {
        return notificationService.sendBulkNotifications(notifications);
    }

    @GetMapping("/{id}")
    public Mono<Notification> getNotification(@PathVariable String id) {
        return notificationService.getNotification(id);
    }

    @GetMapping("/user/{userId}")
    public Flux<Notification> getUserNotifications(@PathVariable String userId) {
        return notificationService.getUserNotifications(userId);
    }

    @GetMapping("/pending")
    public Flux<Notification> getPendingNotifications() {
        return notificationService.getPendingNotifications();
    }

    @GetMapping
    public Flux<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}
