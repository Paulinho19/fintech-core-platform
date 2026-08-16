package com.fintech.core.infrastructure.persistence.account;

import com.fintech.core.application.port.out.AccountRepository;
import com.fintech.core.domain.account.Account;
import com.fintech.core.domain.account.AccountId;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Adapter: implementa a porta AccountRepository (definida em application)
 * usando Spring Data JPA. E a unica classe que sabe que "por tras" tem
 * Postgres via Hibernate - application e domain nunca importam isto.
 */
@Repository
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountSpringDataRepository springDataRepository;
    private final AccountMapper mapper;

    public AccountRepositoryAdapter(AccountSpringDataRepository springDataRepository, AccountMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return springDataRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity saved = springDataRepository.save(mapper.toEntity(account));
        return mapper.toDomain(saved);
    }
}
