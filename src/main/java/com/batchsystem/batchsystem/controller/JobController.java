package com.batchsystem.batchsystem.controller;

import com.batchsystem.batchsystem.modeldto.StatusMessageDto;
import com.batchsystem.batchsystem.service.JobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job")
public class JobController {
    @Autowired
    JobService jobService;

    @Autowired
    JobOperator jobOperator;

    @RequestMapping("launch/{jobName}")
    public ResponseEntity launchJob(@PathVariable String jobName) throws Exception {
        StatusMessageDto responseMsg = new StatusMessageDto();
        try {
            jobService.handle(jobName);

            responseMsg.setStatus(HttpStatus.OK.value());
            responseMsg.setMessage("Job run successfully");

            return ResponseEntity.ok().body(responseMsg);
        } catch(Exception e) {
            responseMsg.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseMsg.setMessage(e.getMessage());

            return ResponseEntity.internalServerError().body(responseMsg);
        }

    }

    @RequestMapping("stop/{jobExecutionId}")
    public String stop(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "Job stop...";
    }


}
