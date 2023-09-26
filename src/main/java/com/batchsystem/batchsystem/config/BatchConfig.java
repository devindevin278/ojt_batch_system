package com.batchsystem.batchsystem.config;

import com.batchsystem.batchsystem.listener.StepSkipListener;
import com.batchsystem.batchsystem.model.Loan;
import com.batchsystem.batchsystem.model.Transaction;
import com.batchsystem.batchsystem.processor.AutopaymentProcessor;
import com.batchsystem.batchsystem.processor.InterestAccrualProcessor;
import com.batchsystem.batchsystem.writer.AutopaymentWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;

@Configuration
public class BatchConfig {

    @Autowired
    public DataSource dataSource;

    @Autowired
    public InterestAccrualProcessor interestAccrualProcessor;

    @Autowired
    public AutopaymentProcessor autopaymentProcessor;
    @Autowired
    public AutopaymentWriter autopaymentWriter;
    @Autowired
    public CompositeItemWriter<Loan> compositeItemWriter;

//    for daily interest accrual
    @Bean
    public Job interestAccrualJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("interestAccrualJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(interestAccrualStep(jobRepository, transactionManager))
                .end()
                .build();
    }

//        for autopayment
    @Bean
    public Job autopaymentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("autopaymentJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(autopaymentStep(jobRepository, transactionManager))
                .end()
                .build();
    }

    public Step interestAccrualStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("interestAccrualStep", jobRepository)
                .chunk(3, transactionManager)
                .allowStartIfComplete(true)
                .reader(jdbcCursorItemReader())
                .processor((ItemProcessor) interestAccrualProcessor)
                .writer(compositeItemWriter)
                .build();
    }

    @Bean
    public Step autopaymentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("autopaymentStep", jobRepository)
                .chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .reader(jdbcCursorItemReader())
                .processor((ItemProcessor) autopaymentProcessor)
                .writer(autopaymentWriter)
                .faultTolerant()
                .listener(stepSkipListener())
                .skipPolicy(skipPolicy())
                .build();
    }


//        public Step addTransactionStep(JobRepository jobRepository)

    public JdbcCursorItemReader<Loan> jdbcCursorItemReader() {
        JdbcCursorItemReader<Loan> jdbcCursorItemReader = new JdbcCursorItemReader<>();

        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql(
                "select * from loan where status_id = 1"
        );
        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>(Loan.class));

        return jdbcCursorItemReader;
    }

    @Bean
    public SkipPolicy skipPolicy() {
        return new ExceptionSkipPolicy();
    }

    @Bean
    public StepSkipListener stepSkipListener() {
        return new StepSkipListener();
    }


//    @Bean
//    public CompositeItemWriter<Loan> compositeItemWriter() {
//        CompositeItemWriter<Loan> compositeItemWriter = new CompositeItemWriter<Loan>();
//        compositeItemWriter.setDelegates(Arrays.asList(loanJdbcBatchItemWriter(), transactionJdbcBatchItemWriter()));
//        return compositeItemWriter;
//    }

//    @Bean
//    public JdbcBatchItemWriter<Loan> loanJdbcBatchItemWriter() {
//
//        JdbcBatchItemWriter<Loan> jdbcBatchItemWriter = new JdbcBatchItemWriter<Loan>();
//
//        jdbcBatchItemWriter.setDataSource(dataSource);
//        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        jdbcBatchItemWriter.setSql(
//                "update loan set interest_amount = ?, status_id = ?, balance = ? where account_id = ?"
//        );
//
//        jdbcBatchItemWriter.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Loan>() {
//            @Override
//            public void setValues(Loan item, PreparedStatement ps) throws SQLException {
//                ps.setDouble(1, item.getInterest_amount());
//                ps.setLong(2, item.getStatus_id());
//                ps.setDouble(3, item.getBalance());
//                ps.setLong(4, item.getAccount_id());
//            }
//        });
//
//        return jdbcBatchItemWriter;
//    }

//    public JdbcBatchItemWriter<Loan> transactionJdbcBatchItemWriter() {
//
//        JdbcBatchItemWriter<Loan> jdbcBatchItemWriter = new JdbcBatchItemWriter<Loan>();
//
//        jdbcBatchItemWriter.setDataSource(dataSource);
//        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        jdbcBatchItemWriter.setSql(
//                "INSERT INTO `transaction`(`nominal`, `loan_id`, `transaction_type_id`, `created`) VALUES (?, ?, ?, ?)"
//        );
//
//        jdbcBatchItemWriter.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Loan>() {
//            @Override
//            public void setValues(Loan item, PreparedStatement ps) throws SQLException {
//                ps.setDouble(1, item.getChanged_amount());
//                ps.setDouble(2, item.getAccount_id());
//                ps.setLong(3, item.getTransactionType());
//                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//            }
//        });
//
//        return jdbcBatchItemWriter;
//    }

}
