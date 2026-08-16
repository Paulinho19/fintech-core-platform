package com.fintech.core.domain.exception;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;

public final class LimitExceededException extends DomainException {

    public LimitExceededException(AccountId accountId, Money dailyLimit, Money attemptedTotal) {
        super("Account %s would exceed daily transfer limit: limit=%s, attemptedTotal=%s"
            .formatted(accountId, dailyLimit.amount(), attemptedTotal.amount()));
    }
}
