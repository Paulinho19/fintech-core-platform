package com.fintech.core.infrastructure.persistence.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.fintech.core.domain.account.Account;
import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Integracao contra Postgres real via Testcontainers - nao H2/mock. Um
 * teste com H2 nao pegaria erro de sintaxe SQL das migrations Flyway nem
 * comportamento especifico do driver Postgres (ex: tipos NUMERIC, UUID).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({AccountRepositoryAdapter.class, AccountMapper.class})
class AccountRepositoryAdapterIT {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private AccountRepositoryAdapter repository;

    @Test
    void savesAndReloadsAccountThroughRealPostgres() {
        Account account = Account.open(AccountId.newId(), Money.brl("250.75"), Money.brl("5000.00"));

        repository.save(account);
        Optional<Account> reloaded = repository.findById(account.id());

        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().balance()).isEqualTo(Money.brl("250.75"));
        assertThat(reloaded.get().dailyTransferLimit()).isEqualTo(Money.brl("5000.00"));
        assertThat(reloaded.get().dailyTransferredAmount()).isEqualTo(Money.brl("0.00"));
    }

    @Test
    void findByIdReturnsEmptyForUnknownAccount() {
        Optional<Account> result = repository.findById(AccountId.newId());

        assertThat(result).isEmpty();
    }

    @Test
    void savePersistsDebitedBalance() {
        Account account = Account.open(AccountId.newId(), Money.brl("100.00"), Money.brl("1000.00"));
        account.debit(Money.brl("30.00"));

        repository.save(account);
        Optional<Account> reloaded = repository.findById(account.id());

        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().balance()).isEqualTo(Money.brl("70.00"));
        assertThat(reloaded.get().dailyTransferredAmount()).isEqualTo(Money.brl("30.00"));
    }
}
