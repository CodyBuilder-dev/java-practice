package com.example.customerbatch.config;

import com.example.customer.core.service.CustomerTierService;
import com.example.customerbatch.client.OrderServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Domain Service 설정
 * - customer-core의 Domain Service를 Spring Bean으로 등록
 */
@Configuration
public class DomainServiceConfig {

    @Bean
    public CustomerTierService customerTierService(OrderServiceClient orderServiceClient) {
        return new CustomerTierService(orderServiceClient);
    }
}
