package com.example.customerbatch.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private CustomerTier tier = CustomerTier.BRONZE;

    // 총 구매 금액 (VIP 등급 산정 기준)
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 마케팅 동의 (1:N 관계)
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MarketingConsent> marketingConsents = new ArrayList<>();

    public Customer() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Customer(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    // 마케팅 동의 추가
    public void addMarketingConsent(MarketingConsent consent) {
        marketingConsents.add(consent);
        consent.setCustomer(this);
    }

    // VIP 등급 갱신 로직
    public void updateTier() {
        if (totalPurchaseAmount.compareTo(new BigDecimal("10000000")) >= 0) {
            this.tier = CustomerTier.DIAMOND;
        } else if (totalPurchaseAmount.compareTo(new BigDecimal("5000000")) >= 0) {
            this.tier = CustomerTier.PLATINUM;
        } else if (totalPurchaseAmount.compareTo(new BigDecimal("1000000")) >= 0) {
            this.tier = CustomerTier.GOLD;
        } else if (totalPurchaseAmount.compareTo(new BigDecimal("500000")) >= 0) {
            this.tier = CustomerTier.SILVER;
        } else {
            this.tier = CustomerTier.BRONZE;
        }
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
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

    public CustomerTier getTier() {
        return tier;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public List<MarketingConsent> getMarketingConsents() {
        return marketingConsents;
    }

    public void setMarketingConsents(List<MarketingConsent> marketingConsents) {
        this.marketingConsents = marketingConsents;
    }
}
