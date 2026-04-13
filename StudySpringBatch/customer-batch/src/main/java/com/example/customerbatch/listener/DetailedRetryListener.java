package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

/**
 * Retry 이벤트를 상세히 추적하는 Listener
 */
public class DetailedRetryListener implements RetryListener {

    private static final Logger logger = LoggerFactory.getLogger(DetailedRetryListener.class);
    private final String jobName;

    public DetailedRetryListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        logger.info("[{}] 🔄 Retry: open() - Retry context opened for item processing",
                jobName);
        return true; // true를 반환해야 retry 진행
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if (throwable == null) {
            logger.info("[{}] 🔄 Retry: close() - Item processed successfully after {} attempt(s)",
                    jobName, context.getRetryCount());
        } else {
            logger.error("[{}] 🔄 Retry: close() - Failed after {} attempt(s). Final exception: {}",
                    jobName, context.getRetryCount(), throwable.getMessage());
        }
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        logger.warn("[{}] 🔄 Retry: onError() - Attempt #{} failed with exception: {} - Message: {}",
                jobName,
                context.getRetryCount(),
                throwable.getClass().getSimpleName(),
                throwable.getMessage());

        if (context.getRetryCount() < 3) { // 최대 재시도 횟수보다 작으면
            logger.info("[{}] 🔄 Retry: Will retry this item (attempt #{} -> #{})",
                    jobName, context.getRetryCount(), context.getRetryCount() + 1);
        } else {
            logger.warn("[{}] 🔄 Retry: Maximum retry attempts reached. Will skip or fail.",
                    jobName);
        }
    }
}
