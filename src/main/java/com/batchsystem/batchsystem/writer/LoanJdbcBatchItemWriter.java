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

@Component
public class LoanJdbcBatchItemWriter extends JdbcBatchItemWriter<Loan> {
    @Autowired
    public LoanJdbcBatchItemWriter(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        setSql("update loan set interest_amount = ?, status_id = ?, balance = ? where account_id = ?");
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Loan>() {
            @Override
            public void setValues(Loan item, PreparedStatement ps) throws SQLException {
                ps.setDouble(1, item.getInterest_amount());
                ps.setLong(2, item.getStatus_id());
                ps.setDouble(3, item.getBalance());
                ps.setLong(4, item.getAccount_id());
            }
        });
    }


}
