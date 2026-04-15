package com.example.customerbatch.partitioner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Customer Tier 기반 Partitioner
 * - 각 고객 등급(BRONZE, SILVER, GOLD, PLATINUM, DIAMOND)별로 파티션 생성
 * - 각 파티션은 별도의 스레드에서 병렬 처리
 */
public class CustomerTierPartitioner implements Partitioner {

    private static final Logger logger = LoggerFactory.getLogger(CustomerTierPartitioner.class);

    private final String[] tiers = {"BRONZE", "SILVER", "GOLD", "PLATINUM", "DIAMOND"};

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("🔀 PARTITIONER: Starting partitioning with gridSize={}", gridSize);
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        Map<String, ExecutionContext> partitions = new HashMap<>();

        // Tier별로 파티션 생성
        for (int i = 0; i < tiers.length && i < gridSize; i++) {
            String tier = tiers[i];
            String partitionName = "partition_" + tier;

            ExecutionContext context = new ExecutionContext();
            context.putString("tier", tier);
            context.putString("partitionName", partitionName);
            context.putInt("partitionNumber", i);

            partitions.put(partitionName, context);

            logger.info("🔀 PARTITIONER: Created partition '{}' for tier '{}'", partitionName, tier);
        }

        logger.info("🔀 PARTITIONER: Total {} partitions created", partitions.size());
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return partitions;
    }
}
