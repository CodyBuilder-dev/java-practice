package com.example.customerbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomerBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerBatchApplication.class, args);
    }
}
