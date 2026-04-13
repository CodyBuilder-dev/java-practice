package com.example.customerbatch.job.partition;

import com.example.customerbatch.exception.RetryableCustomerException;
import com.example.customerbatch.exception.SkippableCustomerException;
import com.example.customerbatch.exception.TemporaryException;
import com.example.customerbatch.listener.*;
import com.example.customerbatch.partitioner.CustomerRangePartitioner;
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
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
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
 * JDBC 기반 Partition 배치 Job (Range 분할)
 * - CustomerRangePartitioner 사용 (offset/limit으로 데이터 분할)
 * - 병렬 처리 시 각 파티션이 독립적인 데이터 범위 처리
 */
@Configuration
public class DebugJdbcPartitionJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(DebugJdbcPartitionJobConfig.class);
    private static final String JOB_NAME = "debugJdbcPartitionJob";
    private static final int CHUNK_SIZE = 5;
    private static final int GRID_SIZE = 4; // 4개 파티션으로 분할
    private static final int SKIP_LIMIT = 10;

    private final Map<String, Integer> processorRetryAttempts = new ConcurrentHashMap<>();
    private final Map<String, Integer> writerRetryAttempts = new ConcurrentHashMap<>();

    @Bean
    public Job debugJdbcPartitionJob(
            JobRepository jobRepository,
            Step debugJdbcPartitionManagerStep
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(debugJdbcPartitionManagerStep)
                .build();
    }

    @Bean
    public Step debugJdbcPartitionManagerStep(
            JobRepository jobRepository,
            TaskExecutorPartitionHandler debugJdbcPartitionHandler,
            DataSource dataSource
    ) {
        return new StepBuilder("debugJdbcPartitionManagerStep", jobRepository)
                .partitioner("debugJdbcPartitionWorkerStep",
                        new CustomerRangePartitioner(dataSource, GRID_SIZE))
                .partitionHandler(debugJdbcPartitionHandler)
                .build();
    }

    @Bean
    public TaskExecutorPartitionHandler debugJdbcPartitionHandler(
            Step debugJdbcPartitionWorkerStep
    ) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setStep(debugJdbcPartitionWorkerStep);
        handler.setGridSize(GRID_SIZE);
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor("jdbc-partition-"));
        return handler;
    }

    @Bean
    public Step debugJdbcPartitionWorkerStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcPagingItemReader<CustomerDto> debugJdbcPartitionReader,
            ItemProcessor<CustomerDto, CustomerDto> debugJdbcPartitionProcessor,
            JdbcBatchItemWriter<CustomerDto> debugJdbcPartitionWriter
    ) {
        return new StepBuilder("debugJdbcPartitionWorkerStep", jobRepository)
                .<CustomerDto, CustomerDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(debugJdbcPartitionReader)
                .processor(debugJdbcPartitionProcessor)
                .writer(debugJdbcPartitionWriter)
                // Listeners
                .listener(new DetailedPartitionListener(JOB_NAME))
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
                .retryLimit(3)
                .skip(SkippableCustomerException.class)
                .skip(TemporaryException.class)
                .skip(RetryableCustomerException.class)
                .skipLimit(SKIP_LIMIT)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<CustomerDto> debugJdbcPartitionReader(
            DataSource dataSource,
            @Value("#{stepExecutionContext['offset']}") Integer offset,
            @Value("#{stepExecutionContext['limit']}") Integer limit,
            @Value("#{stepExecutionContext['partitionName']}") String partitionName
    ) {
        logger.info("[{}] 📖 Creating JDBC Reader for partition: {}, offset: {}, limit: {}",
                JOB_NAME, partitionName, offset, limit);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, name, email, status, tier, last_login_at");
        queryProvider.setFromClause("FROM customers");
        queryProvider.setWhereClause("WHERE status = 'ACTIVE'");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("email", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return new JdbcPagingItemReaderBuilder<CustomerDto>()
                .name("debugJdbcPartitionReader_" + partitionName)
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .pageSize(CHUNK_SIZE)
                .currentItemCount(offset) // 시작 위치
                .maxItemCount(offset + limit) // 종료 위치
                .rowMapper(new CustomerDtoRowMapper())
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<CustomerDto, CustomerDto> debugJdbcPartitionProcessor(
            @Value("#{stepExecutionContext['partitionName']}") String partitionName,
            @Value("#{stepExecutionContext['partitionNumber']}") Integer partitionNumber
    ) {
        return customer -> {
            String email = customer.getEmail();
            String tier = customer.getTier();
            String threadName = Thread.currentThread().getName();
            String key = partitionName + "_" + email;

            int attemptCount = processorRetryAttempts.getOrDefault(key, 0) + 1;
            processorRetryAttempts.put(key, attemptCount);

            logger.info("  [PROCESSOR LOGIC][{}][Thread:{}] Processing: {} (Tier: {}, Attempt: {})",
                    partitionName, threadName, email, tier, attemptCount);

            // 파티션 번호에 따라 다른 에러 시나리오
            // Partition 0: 일시적 에러 → 재시도 성공
            if (partitionNumber == 0 && email.contains("ahn.taeyang")) {
                if (attemptCount == 1) {
                    logger.warn("  [PROCESSOR LOGIC][{}] Partition 0 '{}' - throwing TemporaryException",
                            partitionName, email);
                    throw new TemporaryException("Temporary failure for: " + email);
                } else {
                    logger.info("  [PROCESSOR LOGIC][{}] Partition 0 '{}' - Retry successful!",
                            partitionName, email);
                    processorRetryAttempts.remove(key);
                }
            }

            // Partition 1: 재시도 후 Skip
            if (partitionNumber == 1 && email.contains("go.ara")) {
                logger.warn("  [PROCESSOR LOGIC][{}] Partition 1 '{}' - Attempt #{}, throwing RetryableCustomerException",
                        partitionName, email, attemptCount);
                throw new RetryableCustomerException("Retryable error for: " + email);
            }

            // Partition 2: 즉시 Skip
            if (partitionNumber == 2 && email.contains("nam.joohyuk")) {
                logger.warn("  [PROCESSOR LOGIC][{}] Partition 2 '{}' - throwing SkippableCustomerException",
                        partitionName, email);
                throw new SkippableCustomerException("Skippable error for: " + email);
            }

            // Partition 3: 필터링
            if (partitionNumber == 3 && email.contains("lee.sungkyung")) {
                logger.warn("  [PROCESSOR LOGIC][{}] Partition 3 '{}' - filtering out",
                        partitionName, email);
                return null;
            }

            logger.info("  [PROCESSOR LOGIC][{}] Processing successful for: {}", partitionName, email);
            customer.setTier("DIAMOND");
            customer.setUpdatedAt(LocalDateTime.now());

            return customer;
        };
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<CustomerDto> debugJdbcPartitionWriter(
            DataSource dataSource,
            @Value("#{stepExecutionContext['partitionName']}") String partitionName,
            @Value("#{stepExecutionContext['partitionNumber']}") Integer partitionNumber
    ) {
        return new JdbcBatchItemWriterBuilder<CustomerDto>()
                .dataSource(dataSource)
                .sql("UPDATE customers SET tier = :tier, updated_at = :updatedAt WHERE id = :id")
                .beanMapped()
                .assertUpdates(false)
                .itemPreparedStatementSetter((item, ps) -> {
                    String email = item.getEmail();
                    String threadName = Thread.currentThread().getName();
                    String key = partitionName + "_" + email;

                    int attemptCount = writerRetryAttempts.getOrDefault(key, 0) + 1;
                    writerRetryAttempts.put(key, attemptCount);

                    logger.info("  [WRITER LOGIC][{}][Thread:{}] Writing: {} (Attempt: {})",
                            partitionName, threadName, email, attemptCount);

                    // Writer에서 에러 시뮬레이션 (홀수 파티션에서만)
                    if (partitionNumber % 2 == 1 && email.contains("moon.chaewon")) {
                        if (attemptCount == 1) {
                            logger.warn("  [WRITER LOGIC][{}] Partition {} '{}' - throwing TemporaryException",
                                    partitionName, partitionNumber, email);
                            throw new TemporaryException("Write temporary failure for: " + email);
                        } else {
                            logger.info("  [WRITER LOGIC][{}] Partition {} '{}' - Retry successful!",
                                    partitionName, partitionNumber, email);
                            writerRetryAttempts.remove(key);
                        }
                    }

                    // 정상 처리
                    ps.setString(1, item.getTier());
                    ps.setObject(2, item.getUpdatedAt());
                    ps.setBytes(3, convertUUIDToBytes(item.getId()));

                    logger.info("  [WRITER LOGIC][{}] Successfully prepared statement for: {}", partitionName, email);
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

    public static class CustomerDto {
        private UUID id;
        private String name;
        private String email;
        private String status;
        private String tier;
        private LocalDateTime lastLoginAt;
        private LocalDateTime updatedAt;

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
