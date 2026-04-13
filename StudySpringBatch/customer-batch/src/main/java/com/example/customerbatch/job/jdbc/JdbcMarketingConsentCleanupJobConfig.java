package com.example.customerbatch.job.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
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
 * JDBC 기반 마케팅 동의 만료 처리 배치 Job
 * - JdbcPagingItemReader 사용
 * - JdbcBatchItemWriter로 bulk update
 * - Chunk 기반 처리
 */
@Configuration
public class JdbcMarketingConsentCleanupJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(JdbcMarketingConsentCleanupJobConfig.class);
    private static final int CHUNK_SIZE = 20;

    @Bean
    public Job jdbcMarketingConsentCleanupJob(
            JobRepository jobRepository,
            Step jdbcMarketingConsentCleanupStep
    ) {
        return new JobBuilder("jdbcMarketingConsentCleanupJob", jobRepository)
                .start(jdbcMarketingConsentCleanupStep)
                .build();
    }

    @Bean
    public Step jdbcMarketingConsentCleanupStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcPagingItemReader<MarketingConsentDto> jdbcMarketingConsentReader,
            ItemProcessor<MarketingConsentDto, MarketingConsentDto> jdbcMarketingConsentProcessor,
            JdbcBatchItemWriter<MarketingConsentDto> jdbcMarketingConsentWriter
    ) {
        return new StepBuilder("jdbcMarketingConsentCleanupStep", jobRepository)
                .<MarketingConsentDto, MarketingConsentDto>chunk(CHUNK_SIZE, transactionManager)
                .reader(jdbcMarketingConsentReader)
                .processor(jdbcMarketingConsentProcessor)
                .writer(jdbcMarketingConsentWriter)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<MarketingConsentDto> jdbcMarketingConsentReader(
            DataSource dataSource
    ) {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, customer_id, channel, consented, expires_at");
        queryProvider.setFromClause("FROM marketing_consents");
        queryProvider.setWhereClause("WHERE consented = true AND expires_at < :currentDate");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("expires_at", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentDate", LocalDateTime.now());

        return new JdbcPagingItemReaderBuilder<MarketingConsentDto>()
                .name("jdbcMarketingConsentReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .rowMapper(new MarketingConsentRowMapper())
                .build();
    }

    @Bean
    public ItemProcessor<MarketingConsentDto, MarketingConsentDto> jdbcMarketingConsentProcessor() {
        return consent -> {
            logger.info("[JDBC] Processing expired marketing consent: {} for customer: {} (expired at: {})",
                    consent.getChannel(), consent.getCustomerId(), consent.getExpiresAt());

            // 동의 철회 처리
            consent.setConsented(false);
            consent.setUpdatedAt(LocalDateTime.now());

            logger.info("[JDBC] Marketing consent {} will be revoked", consent.getChannel());

            return consent;
        };
    }

    @Bean
    public JdbcBatchItemWriter<MarketingConsentDto> jdbcMarketingConsentWriter(
            DataSource dataSource
    ) {
        String sql = "UPDATE marketing_consents " +
                "SET consented = :consented, updated_at = :updatedAt " +
                "WHERE id = :id";

        return new JdbcBatchItemWriterBuilder<MarketingConsentDto>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

    /**
     * MarketingConsent DTO for JDBC operations
     */
    public static class MarketingConsentDto {
        private UUID id;
        private UUID customerId;
        private String channel;
        private Boolean consented;
        private LocalDateTime expiresAt;
        private LocalDateTime updatedAt;

        // Getters and Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public UUID getCustomerId() {
            return customerId;
        }

        public void setCustomerId(UUID customerId) {
            this.customerId = customerId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public Boolean getConsented() {
            return consented;
        }

        public void setConsented(Boolean consented) {
            this.consented = consented;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    /**
     * RowMapper for MarketingConsentDto
     */
    private static class MarketingConsentRowMapper implements RowMapper<MarketingConsentDto> {
        @Override
        public MarketingConsentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            MarketingConsentDto dto = new MarketingConsentDto();
            dto.setId(UUID.nameUUIDFromBytes(rs.getBytes("id")));
            dto.setCustomerId(UUID.nameUUIDFromBytes(rs.getBytes("customer_id")));
            dto.setChannel(rs.getString("channel"));
            dto.setConsented(rs.getBoolean("consented"));
            dto.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
            return dto;
        }
    }
}
