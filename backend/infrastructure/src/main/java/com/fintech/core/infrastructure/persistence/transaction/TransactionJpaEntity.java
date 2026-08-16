package com.fintech.core.infrastructure.persistence.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "source_account_id", nullable = false)
    private UUID sourceAccountId;

    @Column(name = "target_key_type", nullable = false, length = 10)
    private String targetKeyType;

    @Column(name = "target_key_value", nullable = false)
    private String targetKeyValue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "settled_at")
    private Instant settledAt;

    protected TransactionJpaEntity() {
        // exigido pelo Hibernate
    }

    public TransactionJpaEntity(UUID id, UUID sourceAccountId, String targetKeyType, String targetKeyValue,
            BigDecimal amount, String currency, String status, Instant createdAt, Instant settledAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetKeyType = targetKeyType;
        this.targetKeyValue = targetKeyValue;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
        this.settledAt = settledAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTargetKeyType() {
        return targetKeyType;
    }

    public String getTargetKeyValue() {
        return targetKeyValue;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSettledAt() {
        return settledAt;
    }
}
