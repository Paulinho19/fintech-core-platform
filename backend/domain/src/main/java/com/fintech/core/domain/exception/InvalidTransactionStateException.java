package com.fintech.core.domain.exception;

import com.fintech.core.domain.transaction.TransactionStatus;

public final class InvalidTransactionStateException extends DomainException {

    public InvalidTransactionStateException(TransactionStatus current, TransactionStatus target) {
        super("Cannot transition transaction from %s to %s".formatted(current, target));
    }
}
