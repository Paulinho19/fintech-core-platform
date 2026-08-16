package com.fintech.core.infrastructure.persistence.account;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AccountSpringDataRepository extends JpaRepository<AccountJpaEntity, UUID> {
}
