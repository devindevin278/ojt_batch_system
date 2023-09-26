package com.batchsystem.batchsystem.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class JobService {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("interestAccrualJob")
    Job interestAccrualJob;

    @Autowired
    @Qualifier("autopaymentJob")
    Job autopaymentJob;

    @Async
    public void handle(@PathVariable String jobName) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        JobExecution jobExecution = null;
        try {
            if(jobName.equals("interestAccrualJob")) {
                jobExecution = jobLauncher.run(interestAccrualJob, jobParameters);
            } else if (jobName.equals("autopaymentJob")) {
                jobExecution = jobLauncher.run(autopaymentJob, jobParameters);
            }
            System.out.println("Job id: " + jobExecution.getJobId());

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Job id: " + jobExecution.getJobId());
        }
    }
}
