package com.batchsystem.batchsystem.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InterestAccrualScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("interestAccrualJob")
    @Autowired
    Job interestAccrualJob;

    //    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void interestAccrualJobStarter() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        try {
            JobExecution jobExecution = null;

            jobExecution = jobLauncher.run(interestAccrualJob, jobParameters);

            System.out.println("job id" + jobExecution.getJobId());
        } catch(Exception e) {
            System.out.println("Exception while starting job");
        }
    }


}
