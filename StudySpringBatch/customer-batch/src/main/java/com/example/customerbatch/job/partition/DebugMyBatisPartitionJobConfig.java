package com.example.customerbatch.job.partition;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.exception.RetryableCustomerException;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.exception.TemporaryException;
import com.example.customerbatch.listener.*;
import com.example.customerbatch.mapper.CustomerBatchMapper;
import com.example.customerbatch.model.CustomerStatus;
import com.example.customerbatch.partitioner.CustomerTierPartitioner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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
 * MyBatis 기반 Partition 배치 Job (Tier별 분할)
 * - Writer에서의 Retry/Skip 동작을 병렬 환경에서 확인
 */
@Configuration
public class DebugMyBatisPartitionJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugMyBatisPartitionJobConfig.class);
    private static final String JOB_NAME = "debugMyBatisPartitionJob";
    private static final int CHUNK_SIZE = 5;
    private static final int GRID_SIZE = 4;
    private static final int SKIP_LIMIT = 10;

    @Autowired
    private CustomerBatchMapper customerBatchMapper;

    private final Map<String, Integer> processorRetryAttempts = new ConcurrentHashMap<>();
    private final Map<String, Integer> writerRetryAttempts = new ConcurrentHashMap<>();

    @Bean
    public Job debugMyBatisPartitionJob(
            JobRepository jobRepository,
            Step debugMyBatisPartitionManagerStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugMyBatisPartitionManagerStep)
                .build();
    }

    @Bean
    public Step debugMyBatisPartitionManagerStep(
            JobRepository jobRepository,
            TaskExecutorPartitionHandler debugMyBatisPartitionHandler
    ) {
        return new StepBuilder("debugMyBatisPartitionManagerStep", jobRepository)
                .partitioner("debugMyBatisPartitionWorkerStep", new CustomerTierPartitioner())
                .partitionHandler(debugMyBatisPartitionHandler)
                .build();
    }

    @Bean
    public TaskExecutorPartitionHandler debugMyBatisPartitionHandler(
            Step debugMyBatisPartitionWorkerStep
    ) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(debugMyBatisPartitionWorkerStep);
        handler.setGridSize(GRID_SIZE);
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor("mybatis-partition-"));
        return handler;
    }

    @Bean
    public Step debugMyBatisPartitionWorkerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MyBatisPagingItemReader<CustomerEntity> debugMyBatisPartitionReader,
            ItemProcessor<CustomerEntity, CustomerEntity> debugMyBatisPartitionProcessor,
            ItemWriter<CustomerEntity> debugMyBatisPartitionWriter
    ) {
        return new StepBuilder("debugMyBatisPartitionWorkerStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugMyBatisPartitionReader)
                .processor(debugMyBatisPartitionProcessor)
                .writer(debugMyBatisPartitionWriter)
                // Listeners
                .listener(new DetailedPartitionListener(JOB_NAME))
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemProcessListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemWriteListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedRetryListener(JOB_NAME))
                // Fault Tolerance
                .faultTolerant()
                .retry(TemporaryException.class)
                .retry(RetryableCustomerException.class)
                .retryLimit(3)
                .skip(SkippableCustomerException.class)
                .skip(TemporaryException.class)
                .skip(RetryableCustomerException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    @Bean
    @StepScope
    public MyBatisPagingItemReader<CustomerEntity> debugMyBatisPartitionReader(
            SqlSessionFactory sqlSessionFactory,
            @Value("#{stepExecutionContext['tier']}") String tier,
            @Value("#{stepExecutionContext['partitionName']}") String partitionName
    ) {
        logger.info("[{}] 📖 Creating MyBatis Reader for partition: {}, tier: {}",
                JOB_NAME, partitionName, tier);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", CustomerStatus.ACTIVE.name());
        parameters.put("tier", tier);

        return new MyBatisPagingItemReaderBuilder<CustomerEntity>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.customerbatch.mapper.CustomerBatchMapper.findByStatusAndTier")
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<CustomerEntity, CustomerEntity> debugMyBatisPartitionProcessor(
            @Value("#{stepExecutionContext['partitionName']}") String partitionName
    ) {
        return customer -> {
            String email = customer.getEmail();
            String tier = customer.getTier().toString();
            String threadName = Thread.currentThread().getName();
            String key = partitionName + "_" + email;

            int attemptCount = processorRetryAttempts.getOrDefault(key, 0) + 1;
            processorRetryAttempts.put(key, attemptCount);

            logger.info("  [PROCESSOR LOGIC][{}][Thread:{}] Processing: {} (Tier: {}, Attempt: {})",
                    partitionName, threadName, email, tier, attemptCount);

            // BRONZE: 재시도 후 성공
            if (tier.equals("BRONZE") && email.contains("lim.jaehyun")) {
                if (attemptCount <= 2) {
                    logger.warn("  [PROCESSOR LOGIC][{}] BRONZE '{}' - Attempt #{}, throwing TemporaryException",
                            partitionName, email, attemptCount);
                    throw new TemporaryException("Temporary failure for: " + email);
                } else {
                    logger.info("  [PROCESSOR LOGIC][{}] BRONZE '{}' - Retry successful on attempt #{}!",
                            partitionName, email, attemptCount);
                    processorRetryAttempts.remove(key);
                }
            }

            // SILVER: 재시도 후 Skip
            if (tier.equals("SILVER") && email.contains("jung.dohyun")) {
                logger.warn("  [PROCESSOR LOGIC][{}] SILVER '{}' - Attempt #{}, throwing RetryableCustomerException",
                        partitionName, email, attemptCount);
                throw new RetryableCustomerException("Retryable error for: " + email);
            }

            logger.info("  [PROCESSOR LOGIC][{}] Processing successful for: {}", partitionName, email);
            return customer;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<CustomerEntity> debugMyBatisPartitionWriter(
            @Value("#{stepExecutionContext['partitionName']}") String partitionName
    ) {
        return items -> {
            String threadName = Thread.currentThread().getName();
            logger.info("  [WRITER LOGIC][{}][Thread:{}] Writing {} items",
                    partitionName, threadName, items.size());

            for (CustomerEntity customer : items) {
                String email = customer.getEmail();
                String tier = customer.getTier().toString();
                String key = partitionName + "_" + email;

                int attemptCount = writerRetryAttempts.getOrDefault(key, 0) + 1;
                writerRetryAttempts.put(key, attemptCount);

                logger.info("  [WRITER LOGIC][{}][Thread:{}] Writing: {} (Tier: {}, Attempt: {})",
                        partitionName, threadName, email, tier, attemptCount);

                // GOLD: Writer에서 재시도 후 성공
                if (tier.equals("GOLD") && email.contains("choi.yejin")) {
                    if (attemptCount == 1) {
                        logger.warn("  [WRITER LOGIC][{}] GOLD '{}' - First attempt, throwing TemporaryException",
                                partitionName, email);
                        throw new TemporaryException("Write temporary failure for: " + email);
                    } else {
                        logger.info("  [WRITER LOGIC][{}] GOLD '{}' - Retry successful on attempt #{}!",
                                partitionName, email, attemptCount);
                        writerRetryAttempts.remove(key);
                    }
                }

                // VIP: Writer에서 재시도 후 Skip
                if (tier.equals("VIP") && email.contains("kim.minji")) {
                    logger.warn("  [WRITER LOGIC][{}] VIP '{}' - Attempt #{}, throwing RetryableCustomerException",
                            partitionName, email, attemptCount);
                    throw new RetryableCustomerException("Write retryable error for: " + email);
                }

                // 정상 처리
                int updated = customerBatchMapper.updateCustomerStatus(
                        customer.getId(),
                        CustomerStatus.ACTIVE.name(),
                        LocalDateTime.now()
                );

                if (updated > 0) {
                    logger.info("  [WRITER LOGIC][{}] Successfully updated: {}", partitionName, email);
                }
            }
        };
    }
}
