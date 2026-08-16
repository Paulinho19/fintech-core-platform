package com.fintech.core.domain.transaction;

import java.util.EnumSet;
import java.util.Set;

public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REVERSED;

    private static final Set<TransactionStatus> NO_TRANSITIONS = EnumSet.noneOf(TransactionStatus.class);

    /** Transicoes validas partindo deste status - a maquina de estados da transacao. */
    public boolean canTransitionTo(TransactionStatus target) {
        return switch (this) {
            case PENDING -> target == COMPLETED || target == FAILED;
            case COMPLETED -> target == REVERSED;
            case FAILED, REVERSED -> NO_TRANSITIONS.contains(target);
        };
    }
}
