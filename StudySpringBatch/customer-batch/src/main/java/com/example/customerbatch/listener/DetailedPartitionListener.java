package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;

import java.time.Duration;

/**
 * Partition 실행을 추적하는 Listener
 * StepExecutionListener를 사용하여 각 파티션의 시작/종료를 추적
 */
public class DetailedPartitionListener implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(DetailedPartitionListener.class);
    private final String jobName;

    public DetailedPartitionListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String partitionName = stepExecution.getExecutionContext().getString("partitionName", "unknown");
        Integer partitionNumber = stepExecution.getExecutionContext().getInt("partitionNumber", -1);
        String tier = stepExecution.getExecutionContext().getString("tier", "");
        Integer offset = stepExecution.getExecutionContext().getInt("offset", -1);
        Integer limit = stepExecution.getExecutionContext().getInt("limit", -1);

        logger.info("╔════════════════════════════════════════════════════════════════╗");
        logger.info("║ [{}}] 🎯 PARTITION START: {}                                    ", jobName, partitionName);
        logger.info("║ Thread: {}", Thread.currentThread().getName());
        logger.info("║ Partition #: {}", partitionNumber);

        if (!tier.isEmpty()) {
            logger.info("║ Tier: {}", tier);
        }
        if (offset >= 0) {
            logger.info("║ Range: offset={}, limit={}", offset, limit);
        }

        logger.info("╚════════════════════════════════════════════════════════════════╝");
    }

    @Override
    public org.springframework.batch.core.ExitStatus afterStep(StepExecution stepExecution) {
        String partitionName = stepExecution.getExecutionContext().getString("partitionName", "unknown");
        long readCount = stepExecution.getReadCount();
        long writeCount = stepExecution.getWriteCount();
        long skipCount = stepExecution.getSkipCount();
        Duration processDuration = Duration.between(stepExecution.getStartTime(), stepExecution.getEndTime());

        logger.info("╔════════════════════════════════════════════════════════════════╗");
        logger.info("║ [{}] ✅ PARTITION END: {}                                      ", jobName, partitionName);
        logger.info("║ Thread: {}", Thread.currentThread().getName());
        logger.info("║ Status: {}", stepExecution.getExitStatus().getExitCode());
        logger.info("║ Read: {}, Write: {}, Skip: {}", readCount, writeCount, skipCount);
        logger.info("║ Duration: {}ms", processDuration);
        logger.info("╚════════════════════════════════════════════════════════════════╝");

        return stepExecution.getExitStatus();
    }
}
