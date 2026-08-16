package com.fintech.core.infrastructure.persistence.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representacao de persistencia de Account. Nunca e o agregado em si -
 * AccountMapper traduz para/de com.fintech.core.domain.account.Account.
 * O dominio nao sabe que esta classe existe (RNF01).
 */
@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "balance_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAmount;

    @Column(name = "daily_transfer_limit_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyTransferLimitAmount;

    @Column(name = "daily_transferred_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyTransferredAmount;

    protected AccountJpaEntity() {
        // exigido pelo Hibernate
    }

    public AccountJpaEntity(UUID id, String currency, BigDecimal balanceAmount,
            BigDecimal dailyTransferLimitAmount, BigDecimal dailyTransferredAmount) {
        this.id = id;
        this.currency = currency;
        this.balanceAmount = balanceAmount;
        this.dailyTransferLimitAmount = dailyTransferLimitAmount;
        this.dailyTransferredAmount = dailyTransferredAmount;
    }

    public UUID getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public BigDecimal getDailyTransferLimitAmount() {
        return dailyTransferLimitAmount;
    }

    public BigDecimal getDailyTransferredAmount() {
        return dailyTransferredAmount;
    }
}
