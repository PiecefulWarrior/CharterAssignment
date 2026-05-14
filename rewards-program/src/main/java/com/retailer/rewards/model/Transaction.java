package com.retailer.rewards.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a single purchase transaction made by a customer.
 * A transaction is the atomic unit on which reward points are calculated.
 */
public class Transaction {

    @NotNull(message = "transactionId must not be null")
    private Long transactionId;

    @NotNull(message = "customerId must not be null")
    private Long customerId;

    @NotNull(message = "amount must not be null")
    @PositiveOrZero(message = "amount must be zero or positive")
    private BigDecimal amount;

    @NotNull(message = "transactionDate must not be null")
    private LocalDate transactionDate;

    public Transaction() {
    }

    public Transaction(Long transactionId, Long customerId, BigDecimal amount, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {

        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {

        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
