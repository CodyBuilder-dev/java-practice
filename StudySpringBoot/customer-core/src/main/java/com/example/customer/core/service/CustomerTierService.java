package com.example.customer.core.service;

import com.example.customer.core.dto.CustomerOrderStats;
import com.example.customer.core.vo.CustomerTier;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Customer Tier Domain Service
 * - 고객 등급 산정 로직
 * - 여러 도메인(Customer + Order) 데이터 조율
 */
public class CustomerTierService {

    private final OrderStatsProvider orderStatsProvider;

    public CustomerTierService(OrderStatsProvider orderStatsProvider) {
        this.orderStatsProvider = orderStatsProvider;
    }

    /**
     * 고객 등급 계산
     * @param customerId 고객 ID
     * @return 계산된 등급
     */
    public CustomerTier calculateTier(UUID customerId) {
        // Order Service로부터 구매 금액 조회
        CustomerOrderStats stats = orderStatsProvider.getOrderStats(customerId);

        BigDecimal totalAmount = stats.getTotalAmount();

        // Value Object의 규칙을 통해 등급 결정
        return CustomerTier.fromPurchaseAmount(totalAmount);
    }

    /**
     * Order Stats Provider Interface
     * - Order Service 데이터 조회를 위한 인터페이스
     * - 구현체는 각 모듈(Service/Batch)에서 제공
     */
    public interface OrderStatsProvider {
        CustomerOrderStats getOrderStats(UUID customerId);
    }
}
