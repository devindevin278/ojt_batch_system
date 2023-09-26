package com.batchsystem.batchsystem.model;

import java.util.Date;

public class Transaction {
    private Double nominal;

    private Date created;

    protected void onCreate() {
        created = new Date();
    }

    private Long loan_id;
    private Long transactionType;

    public Transaction() {
    }

    public Transaction(Double nominal, Date created, Long loan_id, Long transactionType) {
        this.nominal = nominal;
        this.created = created;
        this.loan_id = loan_id;
        this.transactionType = transactionType;
    }

    public Double getNominal() {
        return nominal;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(Long loan_id) {
        this.loan_id = loan_id;
    }

    public Long getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Long transactionType) {
        this.transactionType = transactionType;
    }
}
