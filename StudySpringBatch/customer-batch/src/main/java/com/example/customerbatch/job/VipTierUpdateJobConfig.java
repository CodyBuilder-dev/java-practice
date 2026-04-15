package com.example.customerbatch.job;

import com.example.customerbatch.entity.CustomerEntity;
import com.example.customer.core.model.Customer;
import com.example.customer.core.vo.CustomerTier;
import com.example.customerbatch.repository.CustomerEntityRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VIP 등급 갱신 배치 (DDD 패턴 적용)
 * - Domain Service(CustomerTierService)를 통해 등급 계산
 * - Order Service로부터 구매 금액 조회
 * - 도메인 로직과 인프라 계층 분리
 */
@Configuration
public class VipTierUpdateJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CustomerEntityRepository customerRepository;
    private final com.example.customer.core.service.CustomerTierService tierService;

    public VipTierUpdateJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            CustomerEntityRepository customerRepository,
            com.example.customer.core.service.CustomerTierService tierService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.customerRepository = customerRepository;
        this.tierService = tierService;
    }

    @Bean
    public Job vipTierUpdateJob() {
        return new JobBuilder("vipTierUpdateJob", jobRepository)
                .start(vipTierUpdateStep())
                .build();
    }

    @Bean
    public Step vipTierUpdateStep() {
        return new StepBuilder("vipTierUpdateStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(50, transactionManager)
                .reader(vipTierUpdateReader())
                .processor(vipTierUpdateProcessor())
                .writer(vipTierUpdateWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<CustomerEntity> vipTierUpdateReader() {
        return new RepositoryItemReaderBuilder<CustomerEntity>()
                .name("vipTierUpdateReader")
                .repository(customerRepository)
                .methodName("findAll")
                .pageSize(50)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<CustomerEntity, CustomerEntity> vipTierUpdateProcessor() {
        return entity -> {
            // Entity를 Domain Model로 변환
            Customer customer = entity.toDomain();
            CustomerTier oldTier = entity.getTier();

            // Domain Service를 통해 등급 계산 (Order Service 연동)
            CustomerTier newTier = tierService.calculateTier(customer.getId());

            // 등급 변경 확인
            if (oldTier != newTier) {
                // Entity에 직접 등급 설정
                entity.setTier(newTier);

                System.out.println("Tier updated: Customer=" + customer.getName() +
                        ", " + oldTier + " → " + newTier);
                return entity;
            }

            return null; // 변경 없으면 null 반환
        };
    }

    @Bean
    public ItemWriter<CustomerEntity> vipTierUpdateWriter() {
        return chunk -> {
            List<CustomerEntity> updatedCustomers = chunk.getItems().stream()
                    .collect(Collectors.toUnmodifiableList());

            if (!updatedCustomers.isEmpty()) {
                customerRepository.saveAll(updatedCustomers);
                System.out.println("Updated " + updatedCustomers.size() + " customer tiers");
            }
        };
    }
}
