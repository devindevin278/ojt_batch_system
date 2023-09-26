package com.batchsystem.batchsystem.config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.web.client.HttpClientErrorException;

public class ExceptionSkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {

        return t instanceof NullPointerException || t instanceof HttpClientErrorException;
    }

}
