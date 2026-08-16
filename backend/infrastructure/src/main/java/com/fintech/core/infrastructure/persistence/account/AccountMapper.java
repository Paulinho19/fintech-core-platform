package com.fintech.core.infrastructure.persistence.account;

import com.fintech.core.domain.account.Account;
import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;
import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(
            account.id().value(),
            account.balance().currency().getCurrencyCode(),
            account.balance().amount(),
            account.dailyTransferLimit().amount(),
            account.dailyTransferredAmount().amount()
        );
    }

    public Account toDomain(AccountJpaEntity entity) {
        Currency currency = Currency.getInstance(entity.getCurrency());
        return new Account(
            new AccountId(entity.getId()),
            new Money(entity.getBalanceAmount(), currency),
            new Money(entity.getDailyTransferLimitAmount(), currency),
            new Money(entity.getDailyTransferredAmount(), currency)
        );
    }
}
