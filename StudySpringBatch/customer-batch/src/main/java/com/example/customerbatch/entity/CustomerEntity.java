package com.example.customerbatch.entity;

import com.example.customer.core.enums.CustomerStatus;
import com.example.customer.core.model.Customer;
import com.example.customer.core.vo.CustomerTier;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Customer JPA Entity
 * - customer-core의 Customer 도메인 모델과 매핑
 * - JPA 전용 어노테이션만 포함
 */
@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Enumerated(EnumType.STRING)
    private CustomerTier tier;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MarketingConsentEntity> marketingConsents = new ArrayList<>();

    public CustomerEntity() {}

    /**
     * 도메인 모델로부터 생성
     */
    public static CustomerEntity fromDomain(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setName(customer.getName());
        entity.setEmail(customer.getEmail());
        entity.setStatus(customer.getStatus());
        entity.setTier(customer.getTier());
        entity.setLastLoginAt(customer.getLastLoginAt());
        entity.setCreatedAt(customer.getCreatedAt());
        entity.setUpdatedAt(customer.getUpdatedAt());
        return entity;
    }

    /**
     * 도메인 모델로 변환
     */
    public Customer toDomain() {
        Customer customer = new Customer(id, name, email);
        customer.setStatus(status);
        customer.setTier(tier);
        customer.setLastLoginAt(lastLoginAt);
        customer.setCreatedAt(createdAt);
        customer.setUpdatedAt(updatedAt);
        return customer;
    }

    /**
     * 도메인 모델의 변경사항을 반영
     */
    public void updateFromDomain(Customer customer) {
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.status = customer.getStatus();
        this.tier = customer.getTier();
        this.lastLoginAt = customer.getLastLoginAt();
        this.updatedAt = customer.getUpdatedAt();
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
    }

    public CustomerTier getTier() {
        return tier;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier;
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

    public List<MarketingConsentEntity> getMarketingConsents() {
        return marketingConsents;
    }

    public void setMarketingConsents(List<MarketingConsentEntity> marketingConsents) {
        this.marketingConsents = marketingConsents;
    }
}
