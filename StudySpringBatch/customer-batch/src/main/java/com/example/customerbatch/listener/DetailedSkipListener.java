package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.SkipListener;

/**
 * Skip 이벤트를 상세히 추적하는 Listener
 */
public class DetailedSkipListener<T, S> implements SkipListener<T, S> {

    private static final Logger logger = LoggerFactory.getLogger(DetailedSkipListener.class);
    private final String jobName;
    private int skipCount = 0;

    public DetailedSkipListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        skipCount++;
        logger.warn("[{}] ⚠️  SKIP #{} in READ - Exception: {} - Message: {}",
                jobName, skipCount, t.getClass().getSimpleName(), t.getMessage());
    }

    @Override
    public void onSkipInProcess(T item, Throwable t) {
        skipCount++;
        logger.warn("[{}] ⚠️  SKIP #{} in PROCESS - Item: {} - Exception: {} - Message: {}",
                jobName, skipCount, item, t.getClass().getSimpleName(), t.getMessage());
    }

    @Override
    public void onSkipInWrite(S item, Throwable t) {
        skipCount++;
        logger.warn("[{}] ⚠️  SKIP #{} in WRITE - Item: {} - Exception: {} - Message: {}",
                jobName, skipCount, item, t.getClass().getSimpleName(), t.getMessage());
    }
}
