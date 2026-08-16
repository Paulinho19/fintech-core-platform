package com.fintech.core.domain.event;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;
import com.fintech.core.domain.transaction.TransactionId;
import java.time.Instant;

public record PixTransferCompleted(
    TransactionId transactionId,
    AccountId sourceAccountId,
    AccountId targetAccountId,
    Money amount,
    Instant occurredAt
) implements DomainEvent {
}
