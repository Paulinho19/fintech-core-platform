package com.fintech.core.application.port.out;

import com.fintech.core.domain.transaction.Transaction;
import com.fintech.core.domain.transaction.TransactionId;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findById(TransactionId id);

    Transaction save(Transaction transaction);
}
