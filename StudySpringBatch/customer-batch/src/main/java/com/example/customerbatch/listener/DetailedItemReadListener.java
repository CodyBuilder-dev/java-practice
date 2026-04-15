package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemReadListener;

/**
 * Item Read 과정을 상세히 추적하는 Listener
 */
public class DetailedItemReadListener<T> implements ItemReadListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(DetailedItemReadListener.class);
    private final String jobName;
    private int readCount = 0;

    public DetailedItemReadListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeRead() {
        logger.debug("[{}] 📖 Reader: beforeRead() - Attempting to read item #{}",
                jobName, readCount + 1);
    }

    @Override
    public void afterRead(T item) {
        readCount++;
        if (item != null) {
            logger.info("[{}] 📖 Reader: afterRead() - Successfully read item #{}: {}",
                    jobName, readCount, item);
        } else {
            logger.info("[{}] 📖 Reader: afterRead() - Read returned null (end of data)",
                    jobName);
        }
    }

    @Override
    public void onReadError(Exception ex) {
        logger.error("[{}] ❌ Reader: onReadError() - Error reading item #{}: {}",
                jobName, readCount + 1, ex.getMessage(), ex);
    }
}
