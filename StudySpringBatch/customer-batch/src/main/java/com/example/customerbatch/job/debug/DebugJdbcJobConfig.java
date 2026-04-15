package com.example.customerbatch.job.debug;

import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader;
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 디버깅용 JDBC 배치 Job
 * - 모든 Listener 적용
 * - Skip 정책 설정
 * - Reader/Processor/Writer 각 단계에서 의도적 에러 발생
 */
@Configuration
public class DebugJdbcJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugJdbcJobConfig.class);
    private static final String JOB_NAME = "debugJdbcJob";
    private static final int CHUNK_SIZE = 5;
    private static final int SKIP_LIMIT = 10;

    @Bean
    public Job debugJdbcJob(
            JobRepository jobRepository,
            Step debugJdbcStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugJdbcStep)
                .build();
    }

    @Bean
    public Step debugJdbcStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcPagingItemReader<CustomerDto> debugJdbcReader,
            ItemProcessor<CustomerDto, CustomerDto> debugJdbcProcessor,
            JdbcBatchItemWriter<CustomerDto> debugJdbcWriter
    ) {
        return new StepBuilder("debugJdbcStep", jobRepository)
                .<CustomerDto, CustomerDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugJdbcReader)
                .processor(debugJdbcProcessor)
                .writer(debugJdbcWriter)
                // Listeners
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<CustomerDto>(JOB_NAME))
                .listener(new DetailedItemProcessListener<CustomerDto, CustomerDto>(JOB_NAME))
                .listener(new DetailedItemWriteListener<CustomerDto>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerDto, CustomerDto>(JOB_NAME))
                // Fault Tolerance
                .faultTolerant()
                .skip(SkippableCustomerException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<CustomerDto> debugJdbcReader(DataSource dataSource) throws Exception {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, name, email, status, tier, last_login_at");
        queryProvider.setFromClause("FROM customers");
        queryProvider.setWhereClause("WHERE status = 'ACTIVE'");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("email", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return new JdbcPagingItemReaderBuilder<CustomerDto>()
                .name("debugJdbcReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .pageSize(CHUNK_SIZE)
                .rowMapper(new CustomerDtoRowMapper())
                .build();
    }

    @Bean
    public ItemProcessor<CustomerDto, CustomerDto> debugJdbcProcessor() {
        return customer -> {
            String email = customer.getEmail();

            logger.info("  [PROCESSOR LOGIC] Processing customer: {}", email);

            // 의도적 에러 발생 시나리오
            // 1. "seo.hyunwoo" -> SkippableException
            if (email.contains("seo.hyunwoo")) {
                logger.warn("  [PROCESSOR LOGIC] Found 'seo.hyunwoo' - throwing SkippableCustomerException");
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // 2. "moon.chaewon" -> 필터링
            if (email.contains("moon.chaewon")) {
                logger.warn("  [PROCESSOR LOGIC] Found 'moon.chaewon' - filtering out (returning null)");
                return null;
            }

            // 3. 정상 처리 - tier를 GOLD로 변경
            logger.info("  [PROCESSOR LOGIC] Normal processing for: {}", email);
            customer.setTier("GOLD");
            customer.setUpdatedAt(LocalDateTime.now());

            return customer;
        };
    }

    @Bean
    public JdbcBatchItemWriter<CustomerDto> debugJdbcWriter(DataSource dataSource) {
        String sql = "UPDATE customers " +
                "SET tier = :tier, updated_at = :updatedAt " +
                "WHERE id = :id";

        return new JdbcBatchItemWriterBuilder<CustomerDto>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .assertUpdates(false) // Write 에러 테스트를 위해 false 설정
                .build();
    }

    /**
     * Customer DTO for JDBC operations
     */
    public static class CustomerDto {
        private UUID id;
        private String name;
        private String email;
        private String status;
        private String tier;
        private LocalDateTime lastLoginAt;
        private LocalDateTime updatedAt;

        // Getters and Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTier() {
            return tier;
        }

        public void setTier(String tier) {
            this.tier = tier;
        }

        public LocalDateTime getLastLoginAt() {
            return lastLoginAt;
        }

        public void setLastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public String toString() {
            return "CustomerDto{email='" + email + "', tier='" + tier + "'}";
        }
    }

    /**
     * RowMapper for CustomerDto
     */
    private static class CustomerDtoRowMapper implements RowMapper<CustomerDto> {
        @Override
        public CustomerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerDto dto = new CustomerDto();
            dto.setId(UUID.nameUUIDFromBytes(rs.getBytes("id")));
            dto.setName(rs.getString("name"));
            dto.setEmail(rs.getString("email"));
            dto.setStatus(rs.getString("status"));
            dto.setTier(rs.getString("tier"));

            if (rs.getTimestamp("last_login_at") != null) {
                dto.setLastLoginAt(rs.getTimestamp("last_login_at").toLocalDateTime());
            }

            return dto;
        }
    }
}
