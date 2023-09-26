package com.batchsystem.batchsystem.writer;

import com.batchsystem.batchsystem.model.Loan;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class TransactionJdbcBatchItemWriter extends JdbcBatchItemWriter<Loan> {
    @Autowired
    public TransactionJdbcBatchItemWriter(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        setSql("INSERT INTO `transaction`(`nominal`, `loan_id`, `transaction_type_id`, `created`) VALUES (?, ?, ?, ?)");
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Loan>() {
            @Override
            public void setValues(Loan item, PreparedStatement ps) throws SQLException {
                ps.setDouble(1, item.getChanged_amount());
                ps.setDouble(2, item.getAccount_id());
                ps.setLong(3, item.getTransactionType());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
        });
    }


}
