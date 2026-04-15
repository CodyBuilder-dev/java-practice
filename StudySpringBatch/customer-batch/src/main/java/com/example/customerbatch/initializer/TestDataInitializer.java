package com.example.customerbatch.initializer;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.entity.MarketingConsentEntity;
import com.example.customer.core.enums.CustomerStatus;
import com.example.customer.core.enums.MarketingChannel;
import com.example.customer.core.vo.CustomerTier;

import com.example.customerbatch.repository.CustomerEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 테스트 데이터 초기화
 */
@Component
public class TestDataInitializer implements CommandLineRunner {

    private final CustomerEntityRepository customerEntityRepository;

    public TestDataInitializer(CustomerEntityRepository customerEntityRepository) {
        this.customerEntityRepository = customerEntityRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("========================================");
        System.out.println("Initializing test data...");
        System.out.println("========================================");

        // 1. Alice - GOLD 등급, 최근 로그인, 유효한 마케팅 동의
        CustomerEntity alice = new CustomerEntity("Alice", "alice@example.com");
        alice.setLastLoginAt(LocalDateTime.now().minusDays(10));
        alice.setStatus(CustomerStatus.ACTIVE);
        alice.setTier(CustomerTier.GOLD);
        alice.addMarketingConsent(new MarketingConsentEntity(alice, MarketingChannel.EMAIL, true));
        alice.addMarketingConsent(new MarketingConsentEntity(alice, MarketingChannel.SMS, true));
        customerEntityRepository.save(alice);

        // 2. Bob - BRONZE 등급, 휴면 전환 대상 (100일 전 로그인)
        CustomerEntity bob = new CustomerEntity("Bob", "bob@example.com");
        bob.setLastLoginAt(LocalDateTime.now().minusDays(100));
        bob.setStatus(CustomerStatus.ACTIVE);
        bob.setTier(CustomerTier.BRONZE);
        customerEntityRepository.save(bob);

        // 3. Charlie - SILVER 등급, 휴면 전환 대상, 마케팅 동의 만료
        CustomerEntity charlie = new CustomerEntity("Charlie", "charlie@example.com");
        charlie.setLastLoginAt(LocalDateTime.now().minusDays(95));
        charlie.setStatus(CustomerStatus.ACTIVE);
        charlie.setTier(CustomerTier.SILVER);

        // 만료된 마케팅 동의
        MarketingConsentEntity expiredConsent = new MarketingConsentEntity(charlie, MarketingChannel.EMAIL, true);
        expiredConsent.setExpiresAt(LocalDateTime.now().minusDays(10)); // 10일 전 만료
        charlie.addMarketingConsent(expiredConsent);
        customerEntityRepository.save(charlie);

        // 4. David - BRONZE, 이미 휴면 회원
        CustomerEntity david = new CustomerEntity("David", "david@example.com");
        david.setLastLoginAt(LocalDateTime.now().minusDays(200));
        david.setStatus(CustomerStatus.DORMANT);
        david.setTier(CustomerTier.BRONZE);
        customerEntityRepository.save(david);

        // 5. Emma - PLATINUM 등급, 마케팅 동의 만료
        CustomerEntity emma = new CustomerEntity("Emma", "emma@example.com");
        emma.setLastLoginAt(LocalDateTime.now().minusDays(5));
        emma.setStatus(CustomerStatus.ACTIVE);
        emma.setTier(CustomerTier.PLATINUM);

        MarketingConsentEntity expiredEmail = new MarketingConsentEntity(emma, MarketingChannel.EMAIL, true);
        expiredEmail.setExpiresAt(LocalDateTime.now().minusDays(30)); // 30일 전 만료
        emma.addMarketingConsent(expiredEmail);

        MarketingConsentEntity validSms = new MarketingConsentEntity(emma, MarketingChannel.SMS, true);
        emma.addMarketingConsent(validSms);
        customerEntityRepository.save(emma);

        // 6. Frank - DIAMOND 등급 (최상위)
        CustomerEntity frank = new CustomerEntity("Frank", "frank@example.com");
        frank.setLastLoginAt(LocalDateTime.now().minusDays(1));
        frank.setStatus(CustomerStatus.ACTIVE);
        frank.setTier(CustomerTier.DIAMOND);
        frank.addMarketingConsent(new MarketingConsentEntity(frank, MarketingChannel.EMAIL, true));
        frank.addMarketingConsent(new MarketingConsentEntity(frank, MarketingChannel.SMS, true));
        frank.addMarketingConsent(new MarketingConsentEntity(frank, MarketingChannel.PUSH, true));
        customerEntityRepository.save(frank);

        System.out.println("✓ Test data initialized: 6 customers created");
        System.out.println("  - Dormant conversion targets: Bob, Charlie");
        System.out.println("  - Expired marketing consents: Charlie (EMAIL), Emma (EMAIL)");
        System.out.println("  - Tier distribution: 2 BRONZE, 1 SILVER, 1 GOLD, 1 PLATINUM, 1 DIAMOND");
        System.out.println("========================================\n");
    }
}
