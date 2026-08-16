package com.fintech.core.domain.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fintech.core.domain.exception.InsufficientFundsException;
import com.fintech.core.domain.exception.LimitExceededException;
import com.fintech.core.domain.money.Money;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void debitReducesBalance() {
        Account account = Account.open(AccountId.newId(), Money.brl("100.00"), Money.brl("1000.00"));

        account.debit(Money.brl("30.00"));

        assertThat(account.balance()).isEqualTo(Money.brl("70.00"));
    }

    @Test
    void debitRejectsWhenBalanceInsufficient() {
        Account account = Account.open(AccountId.newId(), Money.brl("10.00"), Money.brl("1000.00"));

        assertThatThrownBy(() -> account.debit(Money.brl("20.00")))
            .isInstanceOf(InsufficientFundsException.class);
        assertThat(account.balance()).isEqualTo(Money.brl("10.00")); // nao mutou em falha
    }

    @Test
    void debitRejectsWhenDailyLimitExceeded() {
        Account account = Account.open(AccountId.newId(), Money.brl("1000.00"), Money.brl("50.00"));

        assertThatThrownBy(() -> account.debit(Money.brl("60.00")))
            .isInstanceOf(LimitExceededException.class);
    }

    @Test
    void debitAccumulatesDailyTransferredAmount() {
        Account account = Account.open(AccountId.newId(), Money.brl("1000.00"), Money.brl("100.00"));

        account.debit(Money.brl("40.00"));
        account.debit(Money.brl("40.00"));

        assertThatThrownBy(() -> account.debit(Money.brl("40.00")))
            .isInstanceOf(LimitExceededException.class);
    }

    @Test
    void resetDailyLimitAllowsTransferringAgain() {
        Account account = Account.open(AccountId.newId(), Money.brl("1000.00"), Money.brl("50.00"));
        account.debit(Money.brl("50.00"));

        account.resetDailyLimit();
        account.debit(Money.brl("50.00"));

        assertThat(account.balance()).isEqualTo(Money.brl("900.00"));
    }

    @Test
    void creditIncreasesBalance() {
        Account account = Account.open(AccountId.newId(), Money.brl("100.00"), Money.brl("1000.00"));

        account.credit(Money.brl("25.00"));

        assertThat(account.balance()).isEqualTo(Money.brl("125.00"));
    }
}
