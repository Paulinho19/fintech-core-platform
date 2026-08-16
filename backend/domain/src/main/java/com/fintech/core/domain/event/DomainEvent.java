package com.fintech.core.domain.event;

import java.time.Instant;

public sealed interface DomainEvent permits PixTransferCompleted, PixTransferFailed {

    Instant occurredAt();
}
