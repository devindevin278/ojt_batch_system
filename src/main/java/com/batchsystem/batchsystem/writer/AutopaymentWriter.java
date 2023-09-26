package com.batchsystem.batchsystem.writer;

import com.batchsystem.batchsystem.model.Loan;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class AutopaymentWriter implements ItemWriter<Loan> {

    @Autowired
    CompositeItemWriter<Loan> compositeItemWriter;

    @Autowired
    JdbcBatchItemWriter<Loan> loanJdbcBatchItemWriter;

    @Override
    public void write(Chunk<? extends Loan> chunk) throws Exception {
        for (Loan item:chunk) {
            System.out.println("Ini writer");
//            no transaction
            if(item.getChanged_amount() == 0.0) {
                loanJdbcBatchItemWriter.write(chunk);
            } else {
                compositeItemWriter.write(chunk);
            }
        }
    }
}
