package com.example.customerbatch.job.debug;

import com.example.customerbatch.exception.RetryableCustomerException;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.exception.TemporaryException;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Retry 정책을 테스트하는 JDBC 배치 Job
 *
 * Reader/Processor/Writer 모든 단계에서의 Retry 동작 확인
 */
@Configuration
public class DebugJdbcRetryJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugJdbcRetryJobConfig.class);
    private static final String JOB_NAME = "debugJdbcRetryJob";
    private static final int CHUNK_SIZE = 5;
    private static final int RETRY_LIMIT = 3;
    private static final int SKIP_LIMIT = 10;

    private final Map<String, Integer> processorRetryAttempts = new ConcurrentHashMap<>();
    private final Map<String, Integer> writerRetryAttempts = new ConcurrentHashMap<>();

    @Bean
    public Job debugJdbcRetryJob(
            JobRepository jobRepository,
            Step debugJdbcRetryStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugJdbcRetryStep)
                .build();
    }

    @Bean
    public Step debugJdbcRetryStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcPagingItemReader<CustomerDto> debugJdbcRetryReader,
            ItemProcessor<CustomerDto, CustomerDto> debugJdbcRetryProcessor,
            JdbcBatchItemWriter<CustomerDto> debugJdbcRetryWriter
    ) {
        return new StepBuilder("debugJdbcRetryStep", jobRepository)
                .<CustomerDto, CustomerDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugJdbcRetryReader)
                .processor(debugJdbcRetryProcessor)
                .writer(debugJdbcRetryWriter)
                // Listeners
                .listener(new DetailedChunkListener(JOB_NAME))
                .listener(new DetailedItemReadListener<CustomerDto>(JOB_NAME))
                .listener(new DetailedItemProcessListener<CustomerDto, CustomerDto>(JOB_NAME))
                .listener(new DetailedItemWriteListener<CustomerDto>(JOB_NAME))
                .listener(new DetailedSkipListener<CustomerDto, CustomerDto>(JOB_NAME))
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
    public JdbcPagingItemReader<CustomerDto> debugJdbcRetryReader(DataSource dataSource) throws Exception {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, name, email, status, tier, last_login_at");
        queryProvider.setFromClause("FROM customers");
        queryProvider.setWhereClause("WHERE status = 'ACTIVE'");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("email", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return new JdbcPagingItemReaderBuilder<CustomerDto>()
                .name("debugJdbcRetryReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .pageSize(CHUNK_SIZE)
                .rowMapper(new CustomerDtoRowMapper())
                .build();
    }

    @Bean
    public ItemProcessor<CustomerDto, CustomerDto> debugJdbcRetryProcessor() {
        return customer -> {
            String email = customer.getEmail();
            int attemptCount = processorRetryAttempts.getOrDefault(email, 0) + 1;
            processorRetryAttempts.put(email, attemptCount);

            logger.info("  [PROCESSOR LOGIC] Processing customer: {} (Attempt #{})", email, attemptCount);

            // 시나리오 1: 일시적 에러 - 2번째 시도에서 성공
            if (email.contains("kang.haneul")) {
                if (attemptCount <= 1) {
                    logger.warn("  [PROCESSOR LOGIC] 'kang.haneul' - Attempt #{}, throwing TemporaryException",
                            attemptCount);
                    throw new TemporaryException("Temporary failure for: " + email);
                } else {
                    logger.info("  [PROCESSOR LOGIC] 'kang.haneul' - Retry successful on attempt #{}!", attemptCount);
                    processorRetryAttempts.remove(email);
                }
            }

            // 시나리오 2: 계속 실패 - 재시도 후 Skip
            if (email.contains("yoon.soobin")) {
                logger.warn("  [PROCESSOR LOGIC] 'yoon.soobin' - Attempt #{}, throwing RetryableCustomerException",
                        attemptCount);
                throw new RetryableCustomerException("Retryable error for: " + email);
            }

            // 시나리오 3: 즉시 Skip (재시도 안함)
            if (email.contains("lim.jaehyun")) {
                logger.warn("  [PROCESSOR LOGIC] 'lim.jaehyun' - Throwing SkippableCustomerException (no retry)");
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // 시나리오 4: 필터링
            if (email.contains("han.yujin")) {
                logger.warn("  [PROCESSOR LOGIC] 'han.yujin' - Filtering out (returning null)");
                return null;
            }

            // 정상 처리
            logger.info("  [PROCESSOR LOGIC] Normal processing for: {}", email);
            customer.setTier("PLATINUM");
            customer.setUpdatedAt(LocalDateTime.now());

            return customer;
        };
    }

    @Bean
    public JdbcBatchItemWriter<CustomerDto> debugJdbcRetryWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerDto>()
                .dataSource(dataSource)
                .sql("UPDATE customers SET tier = :tier, updated_at = :updatedAt WHERE id = :id")
                .beanMapped()
                .assertUpdates(false)
                // ItemPreparedStatementSetter를 사용하여 Writer에서 에러 발생 시뮬레이션
                .itemPreparedStatementSetter((item, ps) -> {
                    String email = item.getEmail();
                    int attemptCount = writerRetryAttempts.getOrDefault(email, 0) + 1;
                    writerRetryAttempts.put(email, attemptCount);

                    logger.info("  [WRITER LOGIC] Writing customer: {} (Attempt #{})", email, attemptCount);

                    // 시나리오 5: Writer에서 일시적 에러 - 재시도 후 성공
                    if (email.contains("song.eunchae")) {
                        if (attemptCount == 1) {
                            logger.warn("  [WRITER LOGIC] 'song.eunchae' - First attempt, throwing TemporaryException");
                            throw new TemporaryException("Write temporary failure for: " + email);
                        } else {
                            logger.info("  [WRITER LOGIC] 'song.eunchae' - Retry successful on attempt #{}!", attemptCount);
                            writerRetryAttempts.remove(email);
                        }
                    }

                    // 시나리오 6: Writer에서 계속 실패 - 재시도 후 Skip
                    if (email.contains("seo.hyunwoo")) {
                        logger.warn("  [WRITER LOGIC] 'seo.hyunwoo' - Attempt #{}, throwing RetryableCustomerException",
                                attemptCount);
                        throw new RetryableCustomerException("Write retryable error for: " + email);
                    }

                    // 정상 처리
                    ps.setString(1, item.getTier());
                    ps.setObject(2, item.getUpdatedAt());
                    ps.setBytes(3, convertUUIDToBytes(item.getId()));

                    logger.info("  [WRITER LOGIC] Successfully prepared statement for customer: {}", email);
                })
                .build();
    }

    private byte[] convertUUIDToBytes(UUID uuid) {
        if (uuid == null) return null;
        byte[] bytes = new byte[16];
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (msb >>> 8 * (7 - i));
            bytes[8 + i] = (byte) (lsb >>> 8 * (7 - i));
        }
        return bytes;
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
