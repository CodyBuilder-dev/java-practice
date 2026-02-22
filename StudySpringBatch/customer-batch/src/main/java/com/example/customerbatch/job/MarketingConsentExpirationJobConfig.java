package com.example.customerbatch.job;

import com.example.customerbatch.model.MarketingConsent;
import com.example.customerbatch.repository.MarketingConsentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 마케팅 동의 만료 처리 배치
 * - 만료된 마케팅 동의를 자동으로 철회 처리
 * - 고객에게 재동의 안내 알림 발송 (향후 확장)
 */
@Configuration
public class MarketingConsentExpirationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MarketingConsentRepository marketingConsentRepository;

    public MarketingConsentExpirationJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MarketingConsentRepository marketingConsentRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.marketingConsentRepository = marketingConsentRepository;
    }

    @Bean
    public Job marketingConsentExpirationJob() {
        return new JobBuilder("marketingConsentExpirationJob", jobRepository)
                .start(marketingConsentExpirationStep())
                .build();
    }

    @Bean
    public Step marketingConsentExpirationStep() {
        return new StepBuilder("marketingConsentExpirationStep", jobRepository)
                .<MarketingConsent, MarketingConsent>chunk(20, transactionManager)
                .reader(expiredConsentReader())
                .processor(expiredConsentProcessor())
                .writer(expiredConsentWriter())
                .build();
    }

    @Bean
    public ItemReader<MarketingConsent> expiredConsentReader() {
        // 만료된 동의 조회
        List<MarketingConsent> expiredConsents =
                marketingConsentRepository.findExpiredConsents(LocalDateTime.now());

        System.out.println("Found " + expiredConsents.size() + " expired marketing consents");
        return new ListItemReader<>(expiredConsents);
    }

    @Bean
    public ItemProcessor<MarketingConsent, MarketingConsent> expiredConsentProcessor() {
        return consent -> {
            // 동의 철회 처리
            consent.revokeConsent();
            System.out.println("Processing expired consent: Customer=" +
                    consent.getCustomer().getName() +
                    ", Channel=" + consent.getChannel());
            return consent;
        };
    }

    @Bean
    public ItemWriter<MarketingConsent> expiredConsentWriter() {
        return chunk -> {
            // DB 저장
            marketingConsentRepository.saveAll(chunk.getItems());
            System.out.println("Revoked " + chunk.size() + " expired marketing consents");

            // 향후 확장: 재동의 요청 알림 발송
            // for (MarketingConsent consent : chunk.getItems()) {
            //     notificationService.sendReconsentRequest(consent.getCustomer(), consent.getChannel());
            // }
        };
    }
}
