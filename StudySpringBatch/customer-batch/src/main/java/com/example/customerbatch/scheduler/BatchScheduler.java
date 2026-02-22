package com.example.customerbatch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 배치 스케줄러
 */
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job dormantCustomerJob;
    private final Job marketingConsentExpirationJob;
    private final Job vipTierUpdateJob;

    public BatchScheduler(
            JobLauncher jobLauncher,
            @Qualifier("dormantCustomerJob") Job dormantCustomerJob,
            @Qualifier("marketingConsentExpirationJob") Job marketingConsentExpirationJob,
            @Qualifier("vipTierUpdateJob") Job vipTierUpdateJob) {
        this.jobLauncher = jobLauncher;
        this.dormantCustomerJob = dormantCustomerJob;
        this.marketingConsentExpirationJob = marketingConsentExpirationJob;
        this.vipTierUpdateJob = vipTierUpdateJob;
    }

    /**
     * 휴면 회원 전환 배치
     * 운영: 매일 새벽 2시 (cron = "0 0 2 * * ?")
     * 테스트: 비활성화
     */
    // @Scheduled(cron = "0 * * * * ?")
    public void runDormantCustomerJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(dormantCustomerJob, params);
            System.out.println("✓ Dormant customer job executed successfully");
        } catch (Exception e) {
            System.err.println("✗ Failed to run dormant customer job: " + e.getMessage());
        }
    }

    /**
     * 마케팅 동의 만료 처리 배치
     * 운영: 매일 새벽 3시 (cron = "0 0 3 * * ?")
     * 테스트: 비활성화
     */
    // @Scheduled(cron = "0 * * * * ?")
    public void runMarketingConsentExpirationJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(marketingConsentExpirationJob, params);
            System.out.println("✓ Marketing consent expiration job executed successfully");
        } catch (Exception e) {
            System.err.println("✗ Failed to run marketing consent expiration job: " + e.getMessage());
        }
    }

    /**
     * VIP 등급 갱신 배치
     * 운영: 매월 1일 새벽 4시 (cron = "0 0 4 1 * ?")
     * 테스트: 비활성화
     */
    // @Scheduled(cron = "0 * * * * ?")
    public void runVipTierUpdateJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(vipTierUpdateJob, params);
            System.out.println("✓ VIP tier update job executed successfully");
        } catch (Exception e) {
            System.err.println("✗ Failed to run VIP tier update job: " + e.getMessage());
        }
    }
}
