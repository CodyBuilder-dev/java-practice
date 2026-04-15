package com.example.customerbatch.job.partition;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.exception.TemporaryException;
import com.example.customerbatch.listener.*;
import com.example.customerbatch.partitioner.CustomerTierPartitioner;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JPA 기반 Partition 배치 Job (Tier별 분할)
 * - CustomerTierPartitioner 사용 (BRONZE, SILVER, GOLD, PLATINUM, DIAMOND)
 * - 각 파티션은 별도 스레드에서 병렬 처리
 * - 모든 Listener 적용하여 병렬 처리 과정 추적
 */
@Configuration
public class DebugJpaPartitionJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugJpaPartitionJobConfig.class);
    private static final String JOB_NAME = "debugJpaPartitionJob";
    private static final int CHUNK_SIZE = 5;
    private static final int GRID_SIZE = 5; // 5개 파티션 (Tier별)
    private static final int SKIP_LIMIT = 10;

    private final Map<String, Integer> retryAttempts = new ConcurrentHashMap<>();

    @Bean
    public Job debugJpaPartitionJob(
            JobRepository jobRepository,
            Step debugJpaPartitionManagerStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugJpaPartitionManagerStep)
                .build();
    }

    /**
     * Manager Step - Partition을 생성하고 Worker Step을 실행
     */
    @Bean
    public Step debugJpaPartitionManagerStep(
            JobRepository jobRepository,
            TaskExecutorPartitionHandler debugJpaPartitionHandler
    ) {
        return new StepBuilder("debugJpaPartitionManagerStep", jobRepository)
                .partitioner("debugJpaPartitionWorkerStep", new CustomerTierPartitioner())
                .partitionHandler(debugJpaPartitionHandler)
                .build();
    }

    /**
     * Partition Handler - 병렬 실행 관리
     */
    @Bean
    public TaskExecutorPartitionHandler debugJpaPartitionHandler(
            Step debugJpaPartitionWorkerStep
    ) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(debugJpaPartitionWorkerStep);
        handler.setGridSize(GRID_SIZE);
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor("partition-"));
        return handler;
    }

    /**
     * Worker Step - 실제 처리 로직
     */
    @Bean
    public Step debugJpaPartitionWorkerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<CustomerEntity> debugJpaPartitionReader,
            ItemProcessor<CustomerEntity, CustomerEntity> debugJpaPartitionProcessor,
            JpaItemWriter<CustomerEntity> debugJpaPartitionWriter
    ) {
        return new StepBuilder("debugJpaPartitionWorkerStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugJpaPartitionReader)
                .processor(debugJpaPartitionProcessor)
                .writer(debugJpaPartitionWriter)
                // Listeners
                .listener(new DetailedPartitionListener(JOB_NAME))
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<>(JOB_NAME))
                .listener(new DetailedItemProcessListener<>(JOB_NAME))
                .listener(new DetailedItemWriteListener<>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedRetryListener(JOB_NAME))
                // Fault Tolerance
                .faultTolerant()
                .retry(TemporaryException.class)
                .retryLimit(3)
                .skip(SkippableCustomerException.class)
                .skip(TemporaryException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    /**
     * Reader - Tier별로 데이터 읽기 (StepScope)
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<CustomerEntity> debugJpaPartitionReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{stepExecutionContext['tier']}") String tier,
            @Value("#{stepExecutionContext['partitionName']}") String partitionName
    ) {
        logger.info("[{}] 📖 Creating Reader for partition: {}, tier: {}",
                JOB_NAME, partitionName, tier);

        String jpqlQuery = "SELECT c FROM CustomerEntity c " +
                "WHERE c.status = :status " +
                "AND c.tier = :tier " +
                "ORDER BY c.email ASC";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", "ACTIVE");
        parameters.put("tier", tier);

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("debugJpaPartitionReader_" + partitionName)
                .entityManagerFactory(entityManagerFactory)
                .queryString(jpqlQuery)
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    /**
     * Processor - 병렬 처리 시 Thread-safe하게 동작
     */
    @Bean
    @StepScope
    public ItemProcessor<CustomerEntity, CustomerEntity> debugJpaPartitionProcessor(
            @Value("#{stepExecutionContext['partitionName']}") String partitionName
    ) {
        return customer -> {
            String email = customer.getEmail();
            String tier = customer.getTier().toString();
            String threadName = Thread.currentThread().getName();
            String key = partitionName + "_" + email;

            int attemptCount = retryAttempts.getOrDefault(key, 0) + 1;
            retryAttempts.put(key, attemptCount);

            logger.info("  [PROCESSOR LOGIC][{}][Thread:{}] Processing: {} (Tier: {}, Attempt: {})",
                    partitionName, threadName, email, tier, attemptCount);

            // 각 Tier별로 다른 에러 시나리오
            // BRONZE: 일시적 에러 → 재시도 성공
            if (tier.equals("BRONZE") && email.contains("han.yujin")) {
                if (attemptCount == 1) {
                    logger.warn("  [PROCESSOR LOGIC][{}] BRONZE customer '{}' - throwing TemporaryException",
                            partitionName, email);
                    throw new TemporaryException("Temporary failure for: " + email);
                } else {
                    logger.info("  [PROCESSOR LOGIC][{}] BRONZE customer '{}' - Retry successful!",
                            partitionName, email);
                    retryAttempts.remove(key);
                }
            }

            // SILVER: Skip
            if (tier.equals("SILVER") && email.contains("kang.haneul")) {
                logger.warn("  [PROCESSOR LOGIC][{}] SILVER customer '{}' - throwing SkippableCustomerException",
                        partitionName, email);
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // GOLD: 필터링
            if (tier.equals("GOLD") && email.contains("park.jiwoo")) {
                logger.warn("  [PROCESSOR LOGIC][{}] GOLD customer '{}' - filtering out",
                        partitionName, email);
                return null;
            }

            // VIP: 정상 처리 (에러 없음)
            logger.info("  [PROCESSOR LOGIC][{}] Processing successful for: {}", partitionName, email);

            customer.setLastLoginAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());

            return customer;
        };
    }

    /**
     * Writer - 각 파티션별로 독립적으로 동작
     */
    @Bean
    @StepScope
    public JpaItemWriter<CustomerEntity> debugJpaPartitionWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaItemWriterBuilder<CustomerEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
