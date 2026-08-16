package com.fintech.core.application.port.out;

import com.fintech.core.domain.account.Account;
import com.fintech.core.domain.account.AccountId;
import java.util.Optional;

/**
 * Porta de saida: o que um use case precisa para persistir/carregar contas,
 * sem saber se por tras tem Postgres, outro banco, ou memoria. A
 * implementacao (adapter) mora em infrastructure.
 */
public interface AccountRepository {

    Optional<Account> findById(AccountId id);

    Account save(Account account);
}
