package com.fintech.core.infrastructure.persistence.transaction;

import com.fintech.core.application.port.out.TransactionRepository;
import com.fintech.core.domain.transaction.Transaction;
import com.fintech.core.domain.transaction.TransactionId;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionSpringDataRepository springDataRepository;
    private final TransactionMapper mapper;

    public TransactionRepositoryAdapter(TransactionSpringDataRepository springDataRepository,
            TransactionMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return springDataRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity saved = springDataRepository.save(mapper.toEntity(transaction));
        return mapper.toDomain(saved);
    }
}
