package com.batchsystem.batchsystem.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutopaymentScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("autopaymentJob")
    @Autowired
    Job autopaymentJob;

//    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void autopaymentJobStarter() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            JobExecution jobExecution = null;

            jobExecution = jobLauncher.run(autopaymentJob, jobParameters);

            System.out.println("job id" + jobExecution.getJobId());
        } catch(Exception e) {
            System.out.println("Exception while starting job");
        }
    }
}
