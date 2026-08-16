package com.fintech.core.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.fintech.core.domain.money.Money;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/**
 * Prova, com concorrencia real (nao simulada), que debit() e seguro dentro
 * de uma unica JVM sob carga de 1000 threads simultaneas - o cenario
 * classico de double spending discutido no fechamento da Etapa 2.
 *
 * O CountDownLatch forca as 1000 threads a saírem juntas no mesmo instante:
 * cada tarefa e submetida (nao executada ainda) e so entao a trava e
 * liberada, senao as tarefas rodariam quase sequencialmente e a corrida
 * nunca apareceria de forma confiavel no teste.
 */
class AccountConcurrencyTest {

    private static final int CONCURRENT_TRANSFERS = 1_000;

    @Test
    void debitUnderHeavyConcurrencyNeverCorruptsBalance() throws Exception {
        Money initialBalance = Money.brl("1000.00");
        Account account = Account.open(AccountId.newId(), initialBalance, Money.brl("1000.00"));

        Result result = runConcurrentDebits(account, Money.brl("1.00"), CONCURRENT_TRANSFERS);

        // Se houvesse lost update, "succeeded" ainda seria 1000, mas o saldo
        // nao bateria com 1000 debitos de fato aplicados (dinheiro "sumiria"
        // do controle sem sair do saldo).
        assertThat(result.succeeded()).isEqualTo(CONCURRENT_TRANSFERS);
        assertThat(result.rejected()).isZero();
        assertThat(account.balance()).isEqualTo(Money.brl("0.00"));
        assertThat(account.dailyTransferredAmount()).isEqualTo(Money.brl("1000.00"));
    }

    @Test
    void debitStopsExactlyWhenBalanceRunsOut() throws Exception {
        // 1000 threads disputam um saldo que so cobre 700 debitos - prova
        // que a checagem de saldo insuficiente tambem se mantem correta sob
        // corrida, nao so o caso "todo mundo cabe".
        Account account = Account.open(AccountId.newId(), Money.brl("700.00"), Money.brl("1000.00"));

        Result result = runConcurrentDebits(account, Money.brl("1.00"), CONCURRENT_TRANSFERS);

        assertThat(result.succeeded()).isEqualTo(700);
        assertThat(result.rejected()).isEqualTo(300);
        assertThat(account.balance()).isEqualTo(Money.brl("0.00"));
    }

    private Result runConcurrentDebits(Account account, Money amountPerTransfer, int concurrentTransfers)
            throws InterruptedException {
        CountDownLatch startingGate = new CountDownLatch(1);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>(concurrentTransfers);
            for (int i = 0; i < concurrentTransfers; i++) {
                futures.add(executor.submit(() -> {
                    startingGate.await();
                    try {
                        account.debit(amountPerTransfer);
                        succeeded.incrementAndGet();
                    } catch (RuntimeException e) {
                        rejected.incrementAndGet();
                    }
                    return null;
                }));
            }

            startingGate.countDown(); // solta as N threads de uma vez so, apos todas submetidas

            for (Future<?> future : futures) {
                future.get(10, TimeUnit.SECONDS);
            }
        } catch (java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
            throw new IllegalStateException("Concurrent debit task failed unexpectedly", e);
        }

        return new Result(succeeded.get(), rejected.get());
    }

    private record Result(int succeeded, int rejected) {
    }
}
