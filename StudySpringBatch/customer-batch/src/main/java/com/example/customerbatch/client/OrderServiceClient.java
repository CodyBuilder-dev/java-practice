package com.example.customerbatch.client;

import com.example.customer.core.dto.CustomerOrderStats;
import com.example.customer.core.service.CustomerTierService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Order Service API 클라이언트
 * - Order 도메인의 데이터를 조회
 * - CustomerTierService.OrderStatsProvider 구현
 */
@Component
public class OrderServiceClient implements CustomerTierService.OrderStatsProvider {

    private final RestTemplate restTemplate;
    private final String orderServiceUrl;

    public OrderServiceClient(
            RestTemplate restTemplate,
            @Value("${order.service.url:http://localhost:8081}") String orderServiceUrl) {
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }

    /**
     * 고객의 주문 통계 조회
     * TODO: 실제 Order Service API 연동
     */
    @Override
    public CustomerOrderStats getOrderStats(UUID customerId) {
        try {
            // String url = orderServiceUrl + "/api/orders/stats/" + customerId;
            // return restTemplate.getForObject(url, CustomerOrderStats.class);

            // 현재는 Mock 데이터 반환 (Order Service 미구현)
            return getMockOrderStats(customerId);
        } catch (Exception e) {
            System.err.println("Failed to fetch order stats: " + e.getMessage());
            return new CustomerOrderStats(customerId, BigDecimal.ZERO, 0);
        }
    }

    /**
     * Mock 데이터 (테스트용)
     * TODO: Order Service 구현 후 제거
     */
    private CustomerOrderStats getMockOrderStats(UUID customerId) {
        // 테스트 데이터와 동일한 금액 반환
        String customerIdStr = customerId.toString();

        // Alice, Emma, Frank 등의 ID 기준으로 Mock 반환
        // 실제로는 DB에서 조회하거나 Order Service API 호출
        BigDecimal mockAmount = BigDecimal.valueOf(1000000); // 기본값

        return new CustomerOrderStats(customerId, mockAmount, 5);
    }
}
