package com.example.customerbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * Chunk 처리 전후를 추적하는 Listener
 */
public class DetailedChunkListener implements ChunkListener {

    private static final Logger logger = LoggerFactory.getLogger(DetailedChunkListener.class);
    private final String jobName;

    public DetailedChunkListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("[{}] 🔵 CHUNK START", jobName);
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("[{}] 🟢 CHUNK END - Committed successfully", jobName);
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        logger.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.error("[{}] 🔴 CHUNK ERROR - Rolling back", jobName);
        logger.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.error("");
    }
}
