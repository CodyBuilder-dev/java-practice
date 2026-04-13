package com.example.customerbatch.job.debug;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.listener.*;
import com.example.customerbatch.mapper.CustomerBatchMapper;
import com.example.customerbatch.model.CustomerStatus;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 디버깅용 MyBatis 배치 Job
 * - 모든 Listener 적용
 * - Skip 정책 설정
 * - 특정 고객에서 의도적으로 에러 발생
 */
@Configuration
public class DebugMyBatisJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugMyBatisJobConfig.class);
    private static final String JOB_NAME = "debugMyBatisJob";
    private static final int CHUNK_SIZE = 5;
    private static final int SKIP_LIMIT = 10;

    @Autowired
    private CustomerBatchMapper customerBatchMapper;

    @Bean
    public Job debugMyBatisJob(
            JobRepository jobRepository,
            Step debugMyBatisStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugMyBatisStep)
                .build();
    }

    @Bean
    public Step debugMyBatisStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MyBatisPagingItemReader<CustomerEntity> debugMyBatisReader,
            ItemProcessor<CustomerEntity, CustomerEntity> debugMyBatisProcessor,
            ItemWriter<CustomerEntity> debugMyBatisWriter
    ) {
        return new StepBuilder("debugMyBatisStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugMyBatisReader)
                .processor(debugMyBatisProcessor)
                .writer(debugMyBatisWriter)
                // Listeners
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemProcessListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                .listener(new DetailedItemWriteListener<CustomerEntity>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerEntity, CustomerEntity>(JOB_NAME))
                // Fault Tolerance
                .faultTolerant()
                .skip(SkippableCustomerException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    @Bean
    public MyBatisPagingItemReader<CustomerEntity> debugMyBatisReader(
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
    public ItemProcessor<CustomerEntity, CustomerEntity> debugMyBatisProcessor() {
        return customer -> {
            String email = customer.getEmail();

            logger.info("  [PROCESSOR LOGIC] Processing customer: {}", email);

            // 의도적 에러 발생 시나리오
            // 1. "kwon" 포함 -> SkippableException
            if (email.contains("kwon.nayeon")) {
                logger.warn("  [PROCESSOR LOGIC] Found 'kwon.nayeon' - throwing SkippableCustomerException");
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // 2. "ahn" 포함 -> 필터링
            if (email.contains("ahn.taeyang")) {
                logger.warn("  [PROCESSOR LOGIC] Found 'ahn.taeyang' - filtering out (returning null)");
                return null;
            }

            // 3. 정상 처리
            logger.info("  [PROCESSOR LOGIC] Normal processing for: {}", email);
            return customer;
        };
    }

    @Bean
    public ItemWriter<CustomerEntity> debugMyBatisWriter() {
        return items -> {
            logger.info("  [WRITER LOGIC] Writing {} items to database", items.size());

            for (CustomerEntity customer : items) {
                // Writer에서 의도적 에러 발생 시나리오
                // "hong" 포함 -> Write 시점에 에러
                if (customer.getEmail().contains("hong.gildong")) {
                    logger.error("  [WRITER LOGIC] Found 'hong.gildong' - throwing SkippableCustomerException in Writer");
                    throw new SkippableCustomerException("Write error for: " + customer.getEmail());
                }

                int updated = customerBatchMapper.updateCustomerStatus(
                        customer.getId(),
                        CustomerStatus.ACTIVE.name(),
                        LocalDateTime.now()
                );

                if (updated > 0) {
                    logger.info("  [WRITER LOGIC] Successfully updated customer: {}", customer.getEmail());
                } else {
                    logger.warn("  [WRITER LOGIC] No rows updated for customer: {}", customer.getEmail());
                }
            }
        };
    }
}
