package com.spring.batchdemo.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleNextConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // If FAILED
                    .to(conditionalJobStep3())
                    .on("*") // Step3 결과에 상관없이
                    .end()
                .from(conditionalJobStep1()) // 이벤트 핸들러 블록
                    .on("*") // Else
                    .to(conditionalJobStep2())
                    .next(conditionalJobStep3())
                    .on("*")
                    .end()
                .end()
                .build();
    }

    private Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>> This is StepNextConditional Step1");

                    // ExitStatus값 지정
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private Step conditionalJobStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>> This is StepNextConditional Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private Step conditionalJobStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>> This is StepNextConditional Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }



}
