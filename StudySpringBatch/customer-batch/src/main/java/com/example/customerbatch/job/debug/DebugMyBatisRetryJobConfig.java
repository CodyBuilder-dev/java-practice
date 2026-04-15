package com.example.customerbatch.job.debug;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.exception.RetryableCustomerException;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.exception.TemporaryException;
import com.example.customerbatch.listener.*;
import com.example.customerbatch.mapper.CustomerBatchMapper;
import com.example.customer.core.enums.CustomerStatus;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Retry 정책을 테스트하는 MyBatis 배치 Job
 *
 * Writer에서의 Retry 동작을 확인
 */
@Configuration
public class DebugMyBatisRetryJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugMyBatisRetryJobConfig.class);
    private static final String JOB_NAME = "debugMyBatisRetryJob";
    private static final int CHUNK_SIZE = 5;
    private static final int RETRY_LIMIT = 3;
    private static final int SKIP_LIMIT = 10;

    @Autowired
    private CustomerBatchMapper customerBatchMapper;

    private final Map<String, Integer> processorRetryAttempts = new ConcurrentHashMap<>();
    private final Map<String, Integer> writerRetryAttempts = new ConcurrentHashMap<>();

    @Bean
    public Job debugMyBatisRetryJob(
            JobRepository jobRepository,
            Step debugMyBatisRetryStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugMyBatisRetryStep)
                .build();
    }

    @Bean
    public Step debugMyBatisRetryStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MyBatisPagingItemReader<CustomerEntity> debugMyBatisRetryReader,
            ItemProcessor<CustomerEntity, CustomerEntity> debugMyBatisRetryProcessor,
            ItemWriter<CustomerEntity> debugMyBatisRetryWriter
    ) {
        return new StepBuilder("debugMyBatisRetryStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugMyBatisRetryReader)
                .processor(debugMyBatisRetryProcessor)
                .writer(debugMyBatisRetryWriter)
                // Listeners
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
                .retryLimit(RETRY_LIMIT)
                .skip(SkippableCustomerException.class)
                .skip(RetryableCustomerException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    @Bean
    public MyBatisPagingItemReader<CustomerEntity> debugMyBatisRetryReader(
            SqlSessionFactory sqlSessionFactory
    ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", CustomerStatus.ACTIVE.name());

        return new MyBatisPagingItemReaderBuilder<CustomerEntity>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.customerbatch.mapper.CustomerBatchMapper.findByStatus")
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity> debugMyBatisRetryProcessor() {
        return customer -> {
            String email = customer.getEmail();
            int attemptCount = processorRetryAttempts.getOrDefault(email, 0) + 1;
            processorRetryAttempts.put(email, attemptCount);

            logger.info("  [PROCESSOR LOGIC] Processing customer: {} (Attempt #{})", email, attemptCount);

            // 시나리오 1: Processor에서 TemporaryException - 재시도 후 성공
            if (email.contains("oh.minseok")) {
                if (attemptCount <= 2) {
                    logger.warn("  [PROCESSOR LOGIC] 'oh.minseok' - Attempt #{}, throwing TemporaryException",
                            attemptCount);
                    throw new TemporaryException("Temporary failure for: " + email);
                } else {
                    logger.info("  [PROCESSOR LOGIC] 'oh.minseok' - Retry successful on attempt #{}!", attemptCount);
                    processorRetryAttempts.remove(email);
                }
            }

            // 시나리오 2: Processor에서 RetryableException - 재시도 후 Skip
            if (email.contains("shin.daeun")) {
                logger.warn("  [PROCESSOR LOGIC] 'shin.daeun' - Attempt #{}, throwing RetryableCustomerException",
                        attemptCount);
                throw new RetryableCustomerException("Retryable error for: " + email);
            }

            logger.info("  [PROCESSOR LOGIC] Normal processing for: {}", email);
            return customer;
        };
    }

    @Bean
    public ItemWriter<CustomerEntity> debugMyBatisRetryWriter() {
        return items -> {
            logger.info("  [WRITER LOGIC] Writing {} items to database", items.size());

            for (CustomerEntity customer : items) {
                String email = customer.getEmail();
                int attemptCount = writerRetryAttempts.getOrDefault(email, 0) + 1;
                writerRetryAttempts.put(email, attemptCount);

                logger.info("  [WRITER LOGIC] Writing customer: {} (Attempt #{})", email, attemptCount);

                // 시나리오 3: Writer에서 TemporaryException - 재시도 후 성공
                if (email.contains("baek.joonho")) {
                    if (attemptCount == 1) {
                        logger.warn("  [WRITER LOGIC] 'baek.joonho' - First attempt, throwing TemporaryException");
                        throw new TemporaryException("Write temporary failure for: " + email);
                    } else {
                        logger.info("  [WRITER LOGIC] 'baek.joonho' - Retry successful on attempt #{}!", attemptCount);
                        writerRetryAttempts.remove(email);
                    }
                }

                // 시나리오 4: Writer에서 RetryableException - 재시도 후 Skip
                if (email.contains("kwon.nayeon")) {
                    logger.warn("  [WRITER LOGIC] 'kwon.nayeon' - Attempt #{}, throwing RetryableCustomerException",
                            attemptCount);
                    throw new RetryableCustomerException("Write retryable error for: " + email);
                }

                // 정상 처리
                int updated = customerBatchMapper.updateCustomerStatus(
                        customer.getId(),
                        CustomerStatus.ACTIVE.name(),
                        LocalDateTime.now()
                );

                if (updated > 0) {
                    logger.info("  [WRITER LOGIC] Successfully updated customer: {}", email);
                } else {
                    logger.warn("  [WRITER LOGIC] No rows updated for customer: {}", email);
                }
            }
        };
    }
}
