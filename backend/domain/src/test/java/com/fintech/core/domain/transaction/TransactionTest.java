package com.fintech.core.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.exception.InvalidTransactionStateException;
import com.fintech.core.domain.money.Money;
import com.fintech.core.domain.pix.EvpKey;
import org.junit.jupiter.api.Test;

class TransactionTest {

    private final AccountId sourceAccountId = AccountId.newId();
    private final EvpKey targetKey = new EvpKey("123e4567-e89b-12d3-a456-426614174000");

    @Test
    void startsAsPending() {
        Transaction transaction = Transaction.createPending(sourceAccountId, targetKey, Money.brl("10.00"));

        assertThat(transaction.status()).isEqualTo(TransactionStatus.PENDING);
        assertThat(transaction.settledAt()).isEmpty();
    }

    @Test
    void completeTransitionsFromPending() {
        Transaction transaction = Transaction.createPending(sourceAccountId, targetKey, Money.brl("10.00"));

        transaction.complete();

        assertThat(transaction.status()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(transaction.settledAt()).isPresent();
    }

    @Test
    void cannotCompleteTwice() {
        Transaction transaction = Transaction.createPending(sourceAccountId, targetKey, Money.brl("10.00"));
        transaction.complete();

        assertThatThrownBy(transaction::complete)
            .isInstanceOf(InvalidTransactionStateException.class);
    }

    @Test
    void cannotFailAfterCompleted() {
        Transaction transaction = Transaction.createPending(sourceAccountId, targetKey, Money.brl("10.00"));
        transaction.complete();

        assertThatThrownBy(transaction::fail)
            .isInstanceOf(InvalidTransactionStateException.class);
    }

    @Test
    void reverseOnlyAllowedAfterCompleted() {
        Transaction transaction = Transaction.createPending(sourceAccountId, targetKey, Money.brl("10.00"));
        transaction.complete();

        transaction.reverse();

        assertThat(transaction.status()).isEqualTo(TransactionStatus.REVERSED);
    }

    @Test
    void rejectsNonPositiveAmount() {
        assertThatThrownBy(() -> Transaction.createPending(sourceAccountId, targetKey, Money.zero(
            java.util.Currency.getInstance("BRL"))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
