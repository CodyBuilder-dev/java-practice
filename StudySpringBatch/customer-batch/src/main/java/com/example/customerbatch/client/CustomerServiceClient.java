package com.example.customerbatch.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Customer Service API 클라이언트
 * - 향후 확장: 쓰기 작업은 API를 통해 처리
 */
@Component
public class CustomerServiceClient {

    private final RestTemplate restTemplate;
    private final String customerServiceUrl;

    public CustomerServiceClient(
            RestTemplate restTemplate,
            @Value("${customer.service.url}") String customerServiceUrl) {
        this.restTemplate = restTemplate;
        this.customerServiceUrl = customerServiceUrl;
    }

    // 예시: 회원 상태 변경 API 호출
    public void updateCustomerStatus(String customerId, String status) {
        String url = customerServiceUrl + "/" + customerId + "/status";
        // restTemplate.put(url, Map.of("status", status));
        System.out.println("Would call API: PUT " + url + " with status=" + status);
    }
}
