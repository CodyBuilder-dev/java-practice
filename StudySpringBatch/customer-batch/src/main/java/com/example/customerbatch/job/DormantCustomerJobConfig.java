package com.example.customerbatch.job;

import com.example.customerbatch.model.Customer;
import com.example.customerbatch.model.CustomerStatus;
import com.example.customerbatch.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 휴면 회원 전환 배치
 * - 90일 이상 로그인하지 않은 활성 회원을 휴면 회원으로 전환
 */
@Configuration
public class DormantCustomerJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CustomerRepository customerRepository;

    public DormantCustomerJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            CustomerRepository customerRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.customerRepository = customerRepository;
    }

    @Bean
    public Job dormantCustomerJob() {
        return new JobBuilder("dormantCustomerJob", jobRepository)
                .start(dormantCustomerStep())
                .build();
    }

    @Bean
    public Step dormantCustomerStep() {
        return new StepBuilder("dormantCustomerStep", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(dormantCustomerReader())
                .processor(dormantCustomerProcessor())
                .writer(dormantCustomerWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<Customer> dormantCustomerReader() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(90);

        return new RepositoryItemReaderBuilder<Customer>()
                .name("dormantCustomerReader")
                .repository(customerRepository)
                .methodName("findByStatusAndLastLoginBefore")
                .arguments(CustomerStatus.ACTIVE, threshold)
                .pageSize(10)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Customer, Customer> dormantCustomerProcessor() {
        return customer -> {
            // 휴면 회원으로 전환
            customer.setStatus(CustomerStatus.DORMANT);
            System.out.println("Processing: " + customer.getName() + " -> DORMANT");
            return customer;
        };
    }

    @Bean
    public ItemWriter<Customer> dormantCustomerWriter() {
        return chunk -> {
            // DB에 저장
            customerRepository.saveAll(chunk.getItems());
            System.out.println("Saved " + chunk.size() + " customers as DORMANT");

            // 향후 확장: 알림 서비스 API 호출
            // notificationService.sendDormantNotification(chunk.getItems());
        };
    }
}
