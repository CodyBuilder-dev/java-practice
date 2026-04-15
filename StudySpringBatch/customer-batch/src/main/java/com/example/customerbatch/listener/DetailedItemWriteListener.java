package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemWriteListener;
import org.springframework.batch.infrastructure.item.Chunk;

/**
 * Item Write 과정을 상세히 추적하는 Listener
 */
public class DetailedItemWriteListener<S> implements ItemWriteListener<S> {

    private static final Logger logger = LoggerFactory.getLogger(DetailedItemWriteListener.class);
    private final String jobName;
    private int writeCount = 0;

    public DetailedItemWriteListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeWrite(Chunk<? extends S> items) {
        logger.info("[{}] 💾 Writer: beforeWrite() - About to write {} items: {}",
                jobName, items.size(), items.getItems());
    }

    @Override
    public void afterWrite(Chunk<? extends S> items) {
        writeCount += items.size();
        logger.info("[{}] 💾 Writer: afterWrite() - Successfully wrote {} items. Total written so far: {}",
                jobName, items.size(), writeCount);
        logger.info("[{}] 💾 Writer: Written items: {}", jobName, items.getItems());
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends S> items) {
        logger.error("[{}] ❌ Writer: onWriteError() - Error writing {} items: {}",
                jobName, items.size(), exception.getMessage(), exception);
        logger.error("[{}] ❌ Writer: Failed items: {}", jobName, items.getItems());
    }
}
