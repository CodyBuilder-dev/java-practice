package com.example.customerbatch.initializer;

import com.example.customerbatch.model.*;
import com.example.customerbatch.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 테스트 데이터 초기화
 */
@Component
public class TestDataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    public TestDataInitializer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("========================================");
        System.out.println("Initializing test data...");
        System.out.println("========================================");

        // 1. Alice - GOLD 등급, 최근 로그인, 유효한 마케팅 동의
        Customer alice = new Customer("Alice", "alice@example.com");
        alice.setLastLoginAt(LocalDateTime.now().minusDays(10));
        alice.setStatus(CustomerStatus.ACTIVE);
        alice.setTotalPurchaseAmount(new BigDecimal("1500000")); // GOLD
        alice.addMarketingConsent(new MarketingConsent(alice, MarketingChannel.EMAIL, true));
        alice.addMarketingConsent(new MarketingConsent(alice, MarketingChannel.SMS, true));
        customerRepository.save(alice);

        // 2. Bob - BRONZE 등급, 휴면 전환 대상 (100일 전 로그인)
        Customer bob = new Customer("Bob", "bob@example.com");
        bob.setLastLoginAt(LocalDateTime.now().minusDays(100));
        bob.setStatus(CustomerStatus.ACTIVE);
        bob.setTotalPurchaseAmount(new BigDecimal("300000")); // BRONZE
        customerRepository.save(bob);

        // 3. Charlie - SILVER 등급, 휴면 전환 대상, 마케팅 동의 만료
        Customer charlie = new Customer("Charlie", "charlie@example.com");
        charlie.setLastLoginAt(LocalDateTime.now().minusDays(95));
        charlie.setStatus(CustomerStatus.ACTIVE);
        charlie.setTotalPurchaseAmount(new BigDecimal("700000")); // SILVER

        // 만료된 마케팅 동의
        MarketingConsent expiredConsent = new MarketingConsent(charlie, MarketingChannel.EMAIL, true);
        expiredConsent.setExpiresAt(LocalDateTime.now().minusDays(10)); // 10일 전 만료
        charlie.addMarketingConsent(expiredConsent);
        customerRepository.save(charlie);

        // 4. David - BRONZE, 이미 휴면 회원
        Customer david = new Customer("David", "david@example.com");
        david.setLastLoginAt(LocalDateTime.now().minusDays(200));
        david.setStatus(CustomerStatus.DORMANT);
        david.setTotalPurchaseAmount(new BigDecimal("100000")); // BRONZE
        customerRepository.save(david);

        // 5. Emma - PLATINUM 등급 (VIP), 마케팅 동의 만료
        Customer emma = new Customer("Emma", "emma@example.com");
        emma.setLastLoginAt(LocalDateTime.now().minusDays(5));
        emma.setStatus(CustomerStatus.ACTIVE);
        emma.setTotalPurchaseAmount(new BigDecimal("6000000")); // PLATINUM

        MarketingConsent expiredEmail = new MarketingConsent(emma, MarketingChannel.EMAIL, true);
        expiredEmail.setExpiresAt(LocalDateTime.now().minusDays(30)); // 30일 전 만료
        emma.addMarketingConsent(expiredEmail);

        MarketingConsent validSms = new MarketingConsent(emma, MarketingChannel.SMS, true);
        emma.addMarketingConsent(validSms);
        customerRepository.save(emma);

        // 6. Frank - DIAMOND 등급 (최상위 VIP)
        Customer frank = new Customer("Frank", "frank@example.com");
        frank.setLastLoginAt(LocalDateTime.now().minusDays(1));
        frank.setStatus(CustomerStatus.ACTIVE);
        frank.setTotalPurchaseAmount(new BigDecimal("15000000")); // DIAMOND
        frank.addMarketingConsent(new MarketingConsent(frank, MarketingChannel.EMAIL, true));
        frank.addMarketingConsent(new MarketingConsent(frank, MarketingChannel.SMS, true));
        frank.addMarketingConsent(new MarketingConsent(frank, MarketingChannel.PUSH, true));
        customerRepository.save(frank);

        System.out.println("✓ Test data initialized: 6 customers created");
        System.out.println("  - Dormant conversion targets: Bob, Charlie");
        System.out.println("  - Expired marketing consents: Charlie (EMAIL), Emma (EMAIL)");
        System.out.println("  - Tier distribution: 2 BRONZE, 1 SILVER, 1 GOLD, 1 PLATINUM, 1 DIAMOND");
        System.out.println("========================================\n");
    }
}
