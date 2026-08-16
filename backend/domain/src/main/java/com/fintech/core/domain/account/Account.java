package com.fintech.core.domain.account;

import com.fintech.core.domain.exception.InsufficientFundsException;
import com.fintech.core.domain.exception.LimitExceededException;
import com.fintech.core.domain.money.Money;
import java.util.Objects;

/**
 * Agregado raiz. Encapsula o unico caminho valido para alterar saldo -
 * debit()/credit() - de forma que violar uma invariante (saldo negativo,
 * estourar limite diario) e fisicamente impossivel fora desta classe.
 *
 * Mutavel por design: e o padrao classico de agregado DDD, e casa bem com
 * o mapeamento JPA que a camada de infrastructure vai fazer por cima dele
 * (a entidade JPA e um mapper para/de este objeto, nunca o objeto em si -
 * RNF01).
 */
public final class Account {

    private final AccountId id;
    private Money balance;
    private final Money dailyTransferLimit;
    private Money dailyTransferredAmount;

    public Account(AccountId id, Money balance, Money dailyTransferLimit, Money dailyTransferredAmount) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.balance = Objects.requireNonNull(balance, "balance must not be null");
        this.dailyTransferLimit = Objects.requireNonNull(dailyTransferLimit, "dailyTransferLimit must not be null");
        this.dailyTransferredAmount =
            Objects.requireNonNull(dailyTransferredAmount, "dailyTransferredAmount must not be null");
    }

    public static Account open(AccountId id, Money initialBalance, Money dailyTransferLimit) {
        return new Account(id, initialBalance, dailyTransferLimit, Money.zero(initialBalance.currency()));
    }

    /**
     * Debita o valor da conta. Lanca InsufficientFundsException se o saldo
     * for menor que o valor pedido, ou LimitExceededException se ultrapassar
     * o limite diario de transferencia - nessa ordem, porque saldo e uma
     * verificacao mais barata e mais fundamental que limite.
     */
    public void debit(Money amount) {
        requirePositive(amount);
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        Money attemptedTotal = dailyTransferredAmount.plus(amount);
        if (attemptedTotal.amount().compareTo(dailyTransferLimit.amount()) > 0) {
            throw new LimitExceededException(id, dailyTransferLimit, attemptedTotal);
        }
        balance = balance.minus(amount);
        dailyTransferredAmount = attemptedTotal;
    }

    public void credit(Money amount) {
        requirePositive(amount);
        balance = balance.plus(amount);
    }

    /** Chamado por um job noturno/agendado - fora do escopo deste agregado decidir quando. */
    public void resetDailyLimit() {
        dailyTransferredAmount = Money.zero(balance.currency());
    }

    private void requirePositive(Money amount) {
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive: " + amount.amount());
        }
    }

    public AccountId id() {
        return id;
    }

    public Money balance() {
        return balance;
    }

    public Money dailyTransferLimit() {
        return dailyTransferLimit;
    }

    public Money dailyTransferredAmount() {
        return dailyTransferredAmount;
    }
}
