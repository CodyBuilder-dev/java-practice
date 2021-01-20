package com.spring.batchdemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class BatchdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchdemoApplication.class, args);
    }

}
