package com.example.customerbatch.job.debug;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.exception.NonSkippableCustomerException;
import com.example.customerbatch.exception.RetryableCustomerException;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.exception.TemporaryException;
import com.example.customerbatch.listener.*;
import com.example.customer.core.enums.CustomerStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Retry 정책을 테스트하는 JPA 배치 Job
 *
 * 시나리오:
 * 1. TemporaryException: 첫 시도 실패 → 재시도 성공
 * 2. RetryableCustomerException: 여러 번 재시도 후 Skip
 * 3. NonSkippableCustomerException: 재시도 후 Job 실패
 */
@Configuration
public class DebugJpaRetryJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugJpaRetryJobConfig.class);
    private static final String JOB_NAME = "debugJpaRetryJob";
    private static final int CHUNK_SIZE = 5;
    private static final int RETRY_LIMIT = 3;
    private static final int SKIP_LIMIT = 10;

    // 재시도 횟수를 추적하기 위한 맵 (이메일 -> 시도 횟수)
    private final Map<String, Integer> retryAttempts = new ConcurrentHashMap<>();

    @Bean
    public Job debugJpaRetryJob(
            JobRepository jobRepository,
            Step debugJpaRetryStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugJpaRetryStep)
                .build();
    }

    @Bean
    public Step debugJpaRetryStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<CustomerEntity> debugJpaRetryReader,
            ItemProcessor<CustomerEntity, CustomerEntity> debugJpaRetryProcessor,
            JpaItemWriter<CustomerEntity> debugJpaRetryWriter
    ) {
        return new StepBuilder("debugJpaRetryStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugJpaRetryReader)
                .processor(debugJpaRetryProcessor)
                .writer(debugJpaRetryWriter)
                // Listeners
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemProcessListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemWriteListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedRetryListener(JOB_NAME))
                // Fault Tolerance - Retry 설정
                .faultTolerant()
                // Retry 정책: 특정 예외는 재시도
                .retry(TemporaryException.class)
                .retry(RetryableCustomerException.class)
                .retryLimit(RETRY_LIMIT)
                // Skip 정책: 재시도 실패 시 Skip 가능한 예외
                .skip(SkippableCustomerException.class)
                .skip(RetryableCustomerException.class) // 재시도 후에도 실패하면 Skip
                .skipLimit(SKIP_LIMIT)
                // NonSkippableCustomerException은 retry도 skip도 안됨 -> Job 실패
                .build();
    }

    @Bean
    public JpaPagingItemReader<CustomerEntity> debugJpaRetryReader(
            EntityManagerFactory entityManagerFactory
    ) {
        String jpqlQuery = "SELECT c FROM CustomerEntity c " +
                "WHERE c.status = :status " +
                "ORDER BY c.email ASC";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", CustomerStatus.ACTIVE);

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("debugJpaRetryReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(jpqlQuery)
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity> debugJpaRetryProcessor() {
        return customer -> {
            String email = customer.getEmail();
            int attemptCount = retryAttempts.getOrDefault(email, 0) + 1;
            retryAttempts.put(email, attemptCount);

            logger.info("  [PROCESSOR LOGIC] Processing customer: {} (Attempt #{})", email, attemptCount);

            // 시나리오 1: TemporaryException - 첫 시도는 실패, 재시도 시 성공
            if (email.contains("kim.minji")) {
                if (attemptCount == 1) {
                    logger.warn("  [PROCESSOR LOGIC] 'kim.minji' - First attempt, throwing TemporaryException");
                    throw new TemporaryException("Temporary failure for: " + email);
                } else {
                    logger.info("  [PROCESSOR LOGIC] 'kim.minji' - Retry successful!");
                    retryAttempts.remove(email); // 성공 시 카운터 리셋
                }
            }

            // 시나리오 2: RetryableCustomerException - 계속 실패 → 재시도 후 Skip
            if (email.contains("lee.seojun")) {
                logger.warn("  [PROCESSOR LOGIC] 'lee.seojun' - Attempt #{}, throwing RetryableCustomerException",
                        attemptCount);
                throw new RetryableCustomerException("Retryable error for: " + email);
            }

            // 시나리오 3: NonSkippableCustomerException - 재시도 후 Job 실패
            // (주의: 이 케이스를 테스트하려면 주석 해제, Job이 실패함)
            /*
            if (email.contains("park.jiwoo")) {
                logger.error("  [PROCESSOR LOGIC] 'park.jiwoo' - Throwing NonSkippableCustomerException");
                throw new NonSkippableCustomerException("Fatal error for: " + email);
            }
            */

            // 시나리오 4: Skip (재시도 없이 바로 Skip)
            if (email.contains("choi.yejin")) {
                logger.warn("  [PROCESSOR LOGIC] 'choi.yejin' - Throwing SkippableCustomerException (no retry)");
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // 시나리오 5: 필터링 (null 반환)
            if (email.contains("jung.dohyun")) {
                logger.warn("  [PROCESSOR LOGIC] 'jung.dohyun' - Filtering out (returning null)");
                return null;
            }

            // 정상 처리
            logger.info("  [PROCESSOR LOGIC] Normal processing for: {}", email);
            customer.setLastLoginAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());

            return customer;
        };
    }

    @Bean
    public JpaItemWriter<CustomerEntity> debugJpaRetryWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaItemWriterBuilder<CustomerEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
