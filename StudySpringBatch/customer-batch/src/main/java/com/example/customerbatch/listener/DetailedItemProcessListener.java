package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

/**
 * Item Process 과정을 상세히 추적하는 Listener
 */
public class DetailedItemProcessListener<T, S> implements ItemProcessListener<T, S> {

    private static final Logger logger = LoggerFactory.getLogger(DetailedItemProcessListener.class);
    private final String jobName;
    private int processCount = 0;

    public DetailedItemProcessListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeProcess(T item) {
        logger.info("[{}] ⚙️  Processor: beforeProcess() - Processing item: {}",
                jobName, item);
    }

    @Override
    public void afterProcess(T item, S result) {
        processCount++;
        if (result != null) {
            logger.info("[{}] ⚙️  Processor: afterProcess() - Item #{} processed successfully. Result: {}",
                    jobName, processCount, result);
        } else {
            logger.warn("[{}] ⚙️  Processor: afterProcess() - Item #{} filtered out (returned null): {}",
                    jobName, processCount, item);
        }
    }

    @Override
    public void onProcessError(T item, Exception e) {
        logger.error("[{}] ❌ Processor: onProcessError() - Error processing item: {}. Error: {}",
                jobName, item, e.getMessage(), e);
    }
}
