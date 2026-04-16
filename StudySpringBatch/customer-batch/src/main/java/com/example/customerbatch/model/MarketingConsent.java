package com.example.customerbatch.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 마케팅 동의 정보
 * - 고객은 여러 채널(이메일, SMS, 푸시)에 대해 각각 동의 가능
 * - 동의는 2년마다 재동의 필요 (개인정보보호법)
 */
@Entity
@Table(name = "marketing_consents")
public class MarketingConsent {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private MarketingChannel channel; // EMAIL, SMS, PUSH

    private Boolean consented = false; // 동의 여부

    private LocalDateTime consentedAt; // 동의 일시
    private LocalDateTime expiresAt;   // 만료 일시 (동의 후 2년)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MarketingConsent() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MarketingConsent(Customer customer, MarketingChannel channel, Boolean consented) {
        this();
        this.customer = customer;
        this.channel = channel;
        this.consented = consented;
        if (consented) {
            this.consentedAt = LocalDateTime.now();
            this.expiresAt = LocalDateTime.now().plusYears(2);
        }
    }

    // 동의 처리
    public void grantConsent() {
        this.consented = true;
        this.consentedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusYears(2);
        this.updatedAt = LocalDateTime.now();
    }

    // 동의 철회
    public void revokeConsent() {
        this.consented = false;
        this.expiresAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    // 만료 여부 확인
    public boolean isExpired() {
        return consented && expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MarketingChannel getChannel() {
        return channel;
    }

    public void setChannel(MarketingChannel channel) {
        this.channel = channel;
    }

    public Boolean getConsented() {
        return consented;
    }

    public void setConsented(Boolean consented) {
        this.consented = consented;
    }

    public LocalDateTime getConsentedAt() {
        return consentedAt;
    }

    public void setConsentedAt(LocalDateTime consentedAt) {
        this.consentedAt = consentedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
