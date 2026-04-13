package com.example.customerbatch.job.mybatis;

import com.example.customerbatch.entity.CustomerEntity;
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
 * MyBatis 기반 휴면 고객 전환 배치 Job
 * - MyBatisPagingItemReader 사용
 * - 90일 이상 로그인하지 않은 고객을 DORMANT 상태로 변경
 * - Chunk 기반 처리
 */
@Configuration
public class MyBatisDormantCustomerJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisDormantCustomerJobConfig.class);
    private static final int CHUNK_SIZE = 10;
    private static final int DORMANT_DAYS = 90;

    @Autowired
    private CustomerBatchMapper customerBatchMapper;

    @Bean
    public Job myBatisDormantCustomerJob(
            JobRepository jobRepository,
            Step myBatisDormantCustomerStep
    ) {
        return new JobBuilder("myBatisDormantCustomerJob", jobRepository)
                .start(myBatisDormantCustomerStep)
                .build();
    }

    @Bean
    public Step myBatisDormantCustomerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            MyBatisPagingItemReader<CustomerEntity> myBatisDormantCustomerReader,
            ItemProcessor<CustomerEntity, CustomerEntity> myBatisDormantCustomerProcessor,
            ItemWriter<CustomerEntity> myBatisDormantCustomerWriter
    ) {
        return new StepBuilder("myBatisDormantCustomerStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(myBatisDormantCustomerReader)
                .processor(myBatisDormantCustomerProcessor)
                .writer(myBatisDormantCustomerWriter)
                .build();
    }

    @Bean
    public MyBatisPagingItemReader<CustomerEntity> myBatisDormantCustomerReader(
            SqlSessionFactory sqlSessionFactory
    ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cutoffDate", LocalDateTime.now().minusDays(DORMANT_DAYS));

        return new MyBatisPagingItemReaderBuilder<CustomerEntity>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.customerbatch.mapper.CustomerBatchMapper.findDormantCustomers")
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity> myBatisDormantCustomerProcessor() {
        return customer -> {
            logger.info("[MyBatis] Processing dormant customer: {} (last login: {})",
                    customer.getEmail(), customer.getLastLoginAt());

            // 휴면 상태로 변경할 고객만 반환
            if (customer.getStatus() == CustomerStatus.ACTIVE &&
                customer.getLastLoginAt() != null &&
                customer.getLastLoginAt().isBefore(LocalDateTime.now().minusDays(DORMANT_DAYS))) {

                logger.info("[MyBatis] Customer {} will be marked as DORMANT", customer.getEmail());
                return customer;
            }

            return null; // 조건에 맞지 않으면 skip
        };
    }

    @Bean
    public ItemWriter<CustomerEntity> myBatisDormantCustomerWriter() {
        return items -> {
            for (CustomerEntity customer : items) {
                int updated = customerBatchMapper.updateCustomerStatus(
                        customer.getId(),
                        CustomerStatus.DORMANT.name(),
                        LocalDateTime.now()
                );

                if (updated > 0) {
                    logger.info("[MyBatis] Customer {} marked as DORMANT successfully",
                            customer.getEmail());
                } else {
                    logger.warn("[MyBatis] Failed to update customer {}", customer.getEmail());
                }
            }
        };
    }
}
