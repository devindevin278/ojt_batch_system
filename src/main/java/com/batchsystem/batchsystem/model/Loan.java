package com.batchsystem.batchsystem.model;

import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

public class Loan {
    private Long account_id;
    private Long cin;
    private Integer pin;
    private Double interest_rate;
    private Double balance;
    private Double interest_amount;
    private Date issue_date;
    private Date maturity_date;
    private Long status_id;
    private Double changed_amount = 0.0;
    private Long transactionType = (long)0;

    public Loan() {
    }

    public Loan(Long account_id, Long cin, Integer pin, Double interest_rate, Double balance, Double interest_amount, Date issue_date, Date maturity_date, Long status_id) {
        this.account_id = account_id;
        this.cin = cin;
        this.pin = pin;
        this.interest_rate = interest_rate;
        this.balance = balance;
        this.interest_amount = interest_amount;
        this.issue_date = issue_date;
        this.maturity_date = maturity_date;
        this.status_id = status_id;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public Long getCin() {
        return cin;
    }

    public void setCin(Long cin) {
        this.cin = cin;
    }


    public Double getChanged_amount() {
        return changed_amount;
    }

    public void setChanged_amount(Double changed_amount) {
        this.changed_amount = changed_amount;
    }

    public Long getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Long transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Double getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(Double interest_rate) {
        this.interest_rate = interest_rate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getInterest_amount() {
        return interest_amount;
    }

    public void setInterest_amount(Double interest_amount) {
        this.interest_amount = interest_amount;
    }

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public Date getMaturity_date() {
        return maturity_date;
    }

    public void setMaturity_date(Date maturity_date) {
        this.maturity_date = maturity_date;
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long status_id) {
        this.status_id = status_id;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "account_id=" + account_id +
                ", cin=" + cin +
                ", pin=" + pin +
                ", interest_rate=" + interest_rate +
                ", balance=" + balance +
                ", interest_amount=" + interest_amount +
                ", issue_date=" + issue_date +
                ", maturity_date=" + maturity_date +
                ", status_id=" + status_id +
                '}';
    }
}
