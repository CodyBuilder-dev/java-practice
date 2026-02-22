package com.example.ecommerce.model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String userId;
    private String type; // EMAIL, SMS, PUSH
    private String recipient;
    private String subject;
    private String content;
    private String status; // PENDING, SENT, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Notification(String id, String userId, String type, String recipient, String subject, String content) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
