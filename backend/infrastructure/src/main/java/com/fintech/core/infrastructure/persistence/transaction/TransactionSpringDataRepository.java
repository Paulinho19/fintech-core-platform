package com.fintech.core.infrastructure.persistence.transaction;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionSpringDataRepository extends JpaRepository<TransactionJpaEntity, UUID> {
}
