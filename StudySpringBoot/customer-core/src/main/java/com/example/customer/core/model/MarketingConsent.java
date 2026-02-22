package com.example.customer.core.model;

import com.example.customer.core.enums.MarketingChannel;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Marketing Consent Entity
 * - 마케팅 동의 정보 관리
 * - 2년 만료 규칙 적용
 */
public class MarketingConsent {
    private UUID id;
    private UUID customerId; // Customer Aggregate 참조
    private MarketingChannel channel;
    private Boolean consented;
    private LocalDateTime consentedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MarketingConsent() {
        this.consented = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MarketingConsent(UUID customerId, MarketingChannel channel, Boolean consented) {
        this();
        this.customerId = customerId;
        this.channel = channel;
        this.consented = consented;
        if (consented) {
            grantConsent();
        }
    }

    /**
     * 동의 처리 (도메인 로직)
     */
    public void grantConsent() {
        this.consented = true;
        this.consentedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusYears(2);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 동의 철회 (도메인 로직)
     */
    public void revokeConsent() {
        this.consented = false;
        this.expiresAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 만료 여부 확인 (도메인 규칙)
     */
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

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
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
