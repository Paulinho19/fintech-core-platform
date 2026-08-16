package com.fintech.core.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;
import com.fintech.core.domain.pix.EvpKey;
import org.junit.jupiter.api.Test;

class TransferResultTest {

    @Test
    void patternMatchingHandlesSuccessAndFailureExhaustively() {
        Transaction transaction = Transaction.createPending(
            AccountId.newId(), new EvpKey("123e4567-e89b-12d3-a456-426614174000"), Money.brl("10.00"));
        TransferResult success = new TransferResult.Success(transaction);
        TransferResult failure = new TransferResult.Failure(FailureReason.INSUFFICIENT_FUNDS, "no balance");

        assertThat(describe(success)).startsWith("OK:");
        assertThat(describe(failure)).isEqualTo("FAIL: INSUFFICIENT_FUNDS");
    }

    // Switch exaustivo sem "default" - se um novo case de TransferResult for
    // adicionado, este metodo para de compilar ate ser atualizado.
    private String describe(TransferResult result) {
        return switch (result) {
            case TransferResult.Success s -> "OK: " + s.transaction().id();
            case TransferResult.Failure f -> "FAIL: " + f.reason();
        };
    }
}
