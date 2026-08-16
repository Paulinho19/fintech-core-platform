package com.fintech.core.domain.event;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.transaction.FailureReason;
import com.fintech.core.domain.transaction.TransactionId;
import java.time.Instant;

public record PixTransferFailed(
    TransactionId transactionId,
    AccountId sourceAccountId,
    FailureReason reason,
    Instant occurredAt
) implements DomainEvent {
}
