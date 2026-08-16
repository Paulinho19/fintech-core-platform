package com.fintech.core.domain.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Valor monetario imutavel. Nunca representa quantia negativa - direcao
 * (debito/credito) e responsabilidade de quem usa Money (ex: Account),
 * nao do valor em si.
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_EVEN);
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Money cannot represent a negative amount: " + amount);
        }
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public static Money of(String amount, Currency currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    public static Money brl(String amount) {
        return of(amount, Currency.getInstance("BRL"));
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        BigDecimal result = amount.subtract(other.amount);
        if (result.signum() < 0) {
            throw new IllegalArgumentException(
                "Resulting amount would be negative: %s - %s".formatted(amount, other.amount));
        }
        return new Money(result, currency);
    }

    public boolean isGreaterThanOrEqualTo(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) >= 0;
    }

    public boolean isLessThan(Money other) {
        return !isGreaterThanOrEqualTo(other);
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    private void requireSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Currency mismatch: %s vs %s".formatted(currency, other.currency));
        }
    }
}
