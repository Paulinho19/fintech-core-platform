package com.fintech.core.domain.transaction;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.exception.InvalidTransactionStateException;
import com.fintech.core.domain.money.Money;
import com.fintech.core.domain.pix.PixKey;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public final class Transaction {

    private final TransactionId id;
    private final AccountId sourceAccountId;
    private final PixKey targetKey;
    private final Money amount;
    private final Instant createdAt;
    private TransactionStatus status;
    private Instant settledAt;

    private Transaction(TransactionId id, AccountId sourceAccountId, PixKey targetKey, Money amount,
            Instant createdAt, TransactionStatus status, Instant settledAt) {
        this.id = Objects.requireNonNull(id);
        this.sourceAccountId = Objects.requireNonNull(sourceAccountId);
        this.targetKey = Objects.requireNonNull(targetKey);
        this.amount = Objects.requireNonNull(amount);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.status = Objects.requireNonNull(status);
        this.settledAt = settledAt;
    }

    public static Transaction createPending(AccountId sourceAccountId, PixKey targetKey, Money amount) {
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Transaction amount must be positive: " + amount.amount());
        }
        return new Transaction(TransactionId.newId(), sourceAccountId, targetKey, amount,
            Instant.now(), TransactionStatus.PENDING, null);
    }

    /**
     * Reconstroi uma transacao a partir de estado ja persistido (ex: linha
     * do banco). Diferente de createPending(), nao aplica regras de criacao
     * de negocio - os dados vindos do banco ja sao confiaveis por definicao,
     * essa fabrica so existe para o mapper de infrastructure usar.
     */
    public static Transaction reconstitute(TransactionId id, AccountId sourceAccountId, PixKey targetKey,
            Money amount, Instant createdAt, TransactionStatus status, Instant settledAt) {
        return new Transaction(id, sourceAccountId, targetKey, amount, createdAt, status, settledAt);
    }

    public void complete() {
        transitionTo(TransactionStatus.COMPLETED);
        settledAt = Instant.now();
    }

    public void fail() {
        transitionTo(TransactionStatus.FAILED);
        settledAt = Instant.now();
    }

    public void reverse() {
        transitionTo(TransactionStatus.REVERSED);
        settledAt = Instant.now();
    }

    private void transitionTo(TransactionStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new InvalidTransactionStateException(status, target);
        }
        status = target;
    }

    public TransactionId id() {
        return id;
    }

    public AccountId sourceAccountId() {
        return sourceAccountId;
    }

    public PixKey targetKey() {
        return targetKey;
    }

    public Money amount() {
        return amount;
    }

    public TransactionStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Optional<Instant> settledAt() {
        return Optional.ofNullable(settledAt);
    }
}
