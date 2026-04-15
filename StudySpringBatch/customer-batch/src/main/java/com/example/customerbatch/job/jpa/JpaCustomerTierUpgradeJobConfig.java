package com.example.customerbatch.job.jpa;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customer.core.vo.CustomerTier;
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

/**
 * JPA 기반 고객 등급 업그레이드 배치 Job
 * - JpaPagingItemReader 사용
 * - JPQL 쿼리로 데이터 읽기
 * - Chunk 기반 처리
 */
@Configuration
public class JpaCustomerTierUpgradeJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(JpaCustomerTierUpgradeJobConfig.class);
    private static final int CHUNK_SIZE = 10;
    private static final double VIP_THRESHOLD = 500000.0; // 50만원 이상

    @Bean
    public Job jpaCustomerTierUpgradeJob(
            JobRepository jobRepository,
            Step jpaCustomerTierUpgradeStep
    ) {
        return new JobBuilder("jpaCustomerTierUpgradeJob", jobRepository)
                .start(jpaCustomerTierUpgradeStep)
                .build();
    }

    @Bean
    public Step jpaCustomerTierUpgradeStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<CustomerEntity> jpaCustomerTierReader,
            ItemProcessor<CustomerEntity, CustomerEntity> jpaCustomerTierProcessor,
            JpaItemWriter<CustomerEntity> jpaCustomerTierWriter
    ) {
        return new StepBuilder("jpaCustomerTierUpgradeStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(CHUNK_SIZE, transactionManager)
                .reader(jpaCustomerTierReader)
                .processor(jpaCustomerTierProcessor)
                .writer(jpaCustomerTierWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<CustomerEntity> jpaCustomerTierReader(
            EntityManagerFactory entityManagerFactory
    ) {
        // 최근 6개월간 주문 금액 합계가 50만원 이상인 고객 조회
        String jpqlQuery = "SELECT c FROM CustomerEntity c " +
                "WHERE c.status = :status " +
                "AND c.tier != :vipTier " +
                "AND EXISTS (" +
                "  SELECT 1 FROM customer_orders o " +
                "  WHERE o.customer_id = c.id " +
                "  AND o.order_date >= :startDate " +
                "  AND o.status = 'COMPLETED' " +
                "  GROUP BY o.customer_id " +
                "  HAVING SUM(o.order_amount) >= :minAmount" +
                ")";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", "ACTIVE");
        parameters.put("vipTier", "VIP");
        parameters.put("startDate", LocalDateTime.now().minusMonths(6));
        parameters.put("minAmount", VIP_THRESHOLD);

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("jpaCustomerTierReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(jpqlQuery)
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity> jpaCustomerTierProcessor() {
        return customer -> {
            logger.info("[JPA] Processing customer for tier upgrade: {} (current tier: {})",
                    customer.getEmail(), customer.getTier());

            // 등급을 DIAMOND로 업그레이드 (VIP 대신)
            String previousTier = customer.getTier().toString();
            customer.setTier(CustomerTier.DIAMOND);
            customer.setUpdatedAt(LocalDateTime.now());

            logger.info("[JPA] Customer {} upgraded: {} -> DIAMOND",
                    customer.getEmail(), previousTier);

            return customer;
        };
    }

    @Bean
    public JpaItemWriter<CustomerEntity> jpaCustomerTierWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaItemWriterBuilder<CustomerEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
