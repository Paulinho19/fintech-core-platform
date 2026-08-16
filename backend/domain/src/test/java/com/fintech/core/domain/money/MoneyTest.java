package com.fintech.core.domain.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void rejectsNegativeAmount() {
        assertThatThrownBy(() -> Money.brl("-1.00"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void plusAddsSameCurrency() {
        Money result = Money.brl("10.00").plus(Money.brl("5.50"));
        assertThat(result).isEqualTo(Money.brl("15.50"));
    }

    @Test
    void minusRejectsNegativeResult() {
        assertThatThrownBy(() -> Money.brl("5.00").minus(Money.brl("10.00")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsCurrencyMismatch() {
        Money usd = Money.of("1.00", java.util.Currency.getInstance("USD"));
        assertThatThrownBy(() -> Money.brl("1.00").plus(usd))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void normalizesScaleToCurrencyDefault() {
        Money money = new Money(new BigDecimal("10"), java.util.Currency.getInstance("BRL"));
        assertThat(money.amount()).isEqualByComparingTo("10.00");
    }
}
