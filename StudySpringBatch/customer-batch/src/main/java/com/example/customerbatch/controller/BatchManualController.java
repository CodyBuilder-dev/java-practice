package com.example.customerbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 배치 수동 실행 컨트롤러 (테스트/관리용)
 */
@RestController
@RequestMapping("/batch")
public class BatchManualController {

    private final JobLauncher jobLauncher;
    private final Job dormantCustomerJob;
    private final Job marketingConsentExpirationJob;
    private final Job vipTierUpdateJob;

    public BatchManualController(
            JobLauncher jobLauncher,
            @Qualifier("dormantCustomerJob") Job dormantCustomerJob,
            @Qualifier("marketingConsentExpirationJob") Job marketingConsentExpirationJob,
            @Qualifier("vipTierUpdateJob") Job vipTierUpdateJob) {
        this.jobLauncher = jobLauncher;
        this.dormantCustomerJob = dormantCustomerJob;
        this.marketingConsentExpirationJob = marketingConsentExpirationJob;
        this.vipTierUpdateJob = vipTierUpdateJob;
    }

    @PostMapping("/dormant-customer")
    public Map<String, String> runDormantCustomerJob() {
        return executeJob(dormantCustomerJob, "Dormant Customer Job");
    }

    @PostMapping("/marketing-consent-expiration")
    public Map<String, String> runMarketingConsentExpirationJob() {
        return executeJob(marketingConsentExpirationJob, "Marketing Consent Expiration Job");
    }

    @PostMapping("/vip-tier-update")
    public Map<String, String> runVipTierUpdateJob() {
        return executeJob(vipTierUpdateJob, "VIP Tier Update Job");
    }

    private Map<String, String> executeJob(Job job, String jobName) {
        Map<String, String> response = new HashMap<>();
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, params);
            response.put("status", "success");
            response.put("message", jobName + " executed successfully");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to run " + jobName + ": " + e.getMessage());
        }
        return response;
    }
}
