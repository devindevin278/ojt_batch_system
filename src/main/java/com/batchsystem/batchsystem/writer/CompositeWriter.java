package com.batchsystem.batchsystem.writer;

import com.batchsystem.batchsystem.model.Loan;
import jakarta.annotation.PostConstruct;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Component
public class CompositeWriter extends CompositeItemWriter<Loan> {

    private final LoanJdbcBatchItemWriter loanJdbcBatchItemWriter;
    private final TransactionJdbcBatchItemWriter transactionJdbcBatchItemWriter;

    @Autowired
    public CompositeWriter(
            @Qualifier("loanJdbcBatchItemWriter") LoanJdbcBatchItemWriter loanJdbcBatchItemWriter,
            @Qualifier("transactionJdbcBatchItemWriter") TransactionJdbcBatchItemWriter transactionJdbcBatchItemWriter

    ) {
        this.loanJdbcBatchItemWriter = loanJdbcBatchItemWriter;
        this.transactionJdbcBatchItemWriter = transactionJdbcBatchItemWriter;
    }

    @PostConstruct
    public void init() {
        setDelegates(Arrays.asList(loanJdbcBatchItemWriter, transactionJdbcBatchItemWriter));
    }

}
