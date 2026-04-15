package com.example.customerbatch.entity;

import com.example.customer.core.enums.MarketingChannel;
import com.example.customer.core.model.MarketingConsent;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MarketingConsent JPA Entity
 */
@Entity
@Table(name = "marketing_consents")
public class MarketingConsentEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Enumerated(EnumType.STRING)
    private MarketingChannel channel;

    private Boolean consented;
    private LocalDateTime consentedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MarketingConsentEntity() {
        this.consented = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MarketingConsentEntity(CustomerEntity customer, MarketingChannel channel, Boolean consented) {
        this();
        this.customer = customer;
        this.channel = channel;
        this.consented = consented;
        if (consented) {
            this.consentedAt = LocalDateTime.now();
            this.expiresAt = LocalDateTime.now().plusYears(2);
        }
    }

    /**
     * 도메인 모델로 변환
     */
    public MarketingConsent toDomain() {
        MarketingConsent consent = new MarketingConsent(
            customer != null ? customer.getId() : null,
            channel,
            consented
        );
        consent.setId(id);
        consent.setConsentedAt(consentedAt);
        consent.setExpiresAt(expiresAt);
        consent.setCreatedAt(createdAt);
        consent.setUpdatedAt(updatedAt);
        return consent;
    }

    /**
     * 만료 여부 확인
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

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
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

    /**
     * 동의 철회 (도메인 로직)
     */
    public void revokeConsent() {
        this.consented = false;
        this.expiresAt = null;
        this.updatedAt = LocalDateTime.now();
    }
}
