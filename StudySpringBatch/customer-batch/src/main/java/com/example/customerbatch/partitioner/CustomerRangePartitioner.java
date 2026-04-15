package com.example.customerbatch.partitioner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Range 기반 Partitioner
 * - 전체 고객 수를 조회하여 균등하게 분할
 * - 각 파티션은 offset과 limit을 가짐
 */
public class CustomerRangePartitioner implements Partitioner {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRangePartitioner.class);

    private final DataSource dataSource;
    private final int gridSize;

    public CustomerRangePartitioner(DataSource dataSource, int gridSize) {
        this.dataSource = dataSource;
        this.gridSize = gridSize;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("🔀 PARTITIONER: Starting range partitioning with gridSize={}", gridSize);
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // 전체 고객 수 조회
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long totalCustomers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM customers WHERE status = 'ACTIVE'",
                Long.class
        );

        if (totalCustomers == null || totalCustomers == 0) {
            logger.warn("🔀 PARTITIONER: No customers found!");
            return new HashMap<>();
        }

        logger.info("🔀 PARTITIONER: Total ACTIVE customers: {}", totalCustomers);

        Map<String, ExecutionContext> partitions = new HashMap<>();
        int partitionSize = (int) Math.ceil((double) totalCustomers / gridSize);

        logger.info("🔀 PARTITIONER: Partition size: {} customers per partition", partitionSize);

        for (int i = 0; i < gridSize; i++) {
            int offset = i * partitionSize;
            int limit = partitionSize;
            String partitionName = "partition_" + i;

            // 마지막 파티션은 나머지 모두 포함
            if (i == gridSize - 1) {
                limit = (int) (totalCustomers - offset);
            }

            ExecutionContext context = new ExecutionContext();
            context.putInt("offset", offset);
            context.putInt("limit", limit);
            context.putString("partitionName", partitionName);
            context.putInt("partitionNumber", i);

            partitions.put(partitionName, context);

            logger.info("🔀 PARTITIONER: Created partition '{}' - offset: {}, limit: {}",
                    partitionName, offset, limit);
        }

        logger.info("🔀 PARTITIONER: Total {} partitions created", partitions.size());
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return partitions;
    }
}
