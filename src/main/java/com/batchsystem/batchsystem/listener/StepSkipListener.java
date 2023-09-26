package com.batchsystem.batchsystem.listener;

import com.batchsystem.batchsystem.model.Loan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;

public class StepSkipListener implements SkipListener<Loan, Number> {
//    StepExecution
    Logger logger = LoggerFactory.getLogger(StepSkipListener.class);

    @Override
    public void onSkipInRead(Throwable t) {
        SkipListener.super.onSkipInRead(t);
        logger.info("A failure on read {}", t.getMessage());
    }

    @Override
    public void onSkipInWrite(Number item, Throwable t) {
        SkipListener.super.onSkipInWrite(item, t);
        logger.info("A failure on write {}, {}", t.getMessage(), item);
    }

    @SneakyThrows
    @Override
    public void onSkipInProcess(Loan item, Throwable t) {
        SkipListener.super.onSkipInProcess(item, t);
        try {
            logger.info("Item {} was skipped due to exception {}", new ObjectMapper().writeValueAsString(item), t.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
