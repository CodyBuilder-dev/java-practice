package com.spring.batchdemo.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // Log4j의 Lombok용 어노테이션
@RequiredArgsConstructor // 생성자 DI Lombok 어노테이션
@Configuration
public class SimpleJobConfiguration {
    // JobBuilder, StepBuilder 의존성을 주입한다
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    // Job을 정의한다
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1(null))
                .build();
    }

    @Bean
    @JobScope
    // Step을 정의한다
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        // @Value 어노테이션을 이용해, 명령줄 argument를 받아온다.
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution,chunkContext) -> {
                    log.info(">>> This is Step1");
                    log.info(">>> requestDate = {}",requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
