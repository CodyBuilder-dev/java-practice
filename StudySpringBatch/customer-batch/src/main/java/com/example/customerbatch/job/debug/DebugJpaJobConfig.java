package com.example.customerbatch.job.debug;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.listener.*;
import com.example.customerbatch.model.CustomerStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 디버깅용 JPA 배치 Job
 * - 모든 Listener 적용
 * - Skip 정책 설정 (SkippableCustomerException은 skip, 나머지는 fail)
 * - 특정 이메일 패턴에서 의도적으로 에러 발생
 */
@Configuration
public class DebugJpaJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugJpaJobConfig.class);
    private static final String JOB_NAME = "debugJpaJob";
    private static final int CHUNK_SIZE = 5;
    private static final int SKIP_LIMIT = 10;

    @Bean
    public Job debugJpaJob(
            JobRepository jobRepository,
            Step debugJpaStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugJpaStep)
                .build();
    }

    @Bean
    public Step debugJpaStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<CustomerEntity> debugJpaReader,
            ItemProcessor<CustomerEntity, CustomerEntity> debugJpaProcessor,
            JpaItemWriter<CustomerEntity> debugJpaWriter
    ) {
        return new StepBuilder("debugJpaStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugJpaReader)
                .processor(debugJpaProcessor)
                .writer(debugJpaWriter)
                // Listeners
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemProcessListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemWriteListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                // Fault Tolerance - Skip 설정
                .faultTolerant()
                .skip(SkippableCustomerException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    @Bean
    public JpaPagingItemReader<CustomerEntity> debugJpaReader(
            EntityManagerFactory entityManagerFactory
    ) {
        String jpqlQuery = "SELECT c FROM CustomerEntity c " +
                "WHERE c.status = :status " +
                "ORDER BY c.email ASC";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", CustomerStatus.ACTIVE);

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("debugJpaReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(jpqlQuery)
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity> debugJpaProcessor() {
        return customer -> {
            String email = customer.getEmail();

            logger.info("  [PROCESSOR LOGIC] Processing customer: {}", email);

            // 의도적 에러 발생 시나리오
            // 1. "skip" 포함된 이메일 -> SkippableException (skip됨)
            if (email.contains("shin.daeun")) {
                logger.warn("  [PROCESSOR LOGIC] Found 'shin.daeun' in email - throwing SkippableCustomerException");
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // 2. "filter" 포함된 이메일 -> null 리턴 (필터링)
            if (email.contains("baek.joonho")) {
                logger.warn("  [PROCESSOR LOGIC] Found 'baek.joonho' in email - filtering out (returning null)");
                return null; // 필터링 (Write에 전달되지 않음)
            }

            // 3. 정상 처리 - last login 업데이트
            logger.info("  [PROCESSOR LOGIC] Normal processing for: {}", email);
            customer.setLastLoginAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());

            return customer;
        };
    }

    @Bean
    public JpaItemWriter<CustomerEntity> debugJpaWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaItemWriterBuilder<CustomerEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
