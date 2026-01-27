package com.math713.customercli.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Integer id;
    private Integer customerId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Transaction(Integer customerId, TransactionType type, BigDecimal amount) {
        this.customerId = customerId;
        this.type = type;
        this.amount = amount;
    }

    public Transaction(Integer id, Integer customerId, TransactionType type,
                       BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }
}

