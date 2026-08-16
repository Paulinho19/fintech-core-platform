package com.fintech.core.domain.exception;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;

public final class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(AccountId accountId, Money balance, Money requested) {
        super("Account %s has insufficient funds: balance=%s, requested=%s"
            .formatted(accountId, balance.amount(), requested.amount()));
    }
}
