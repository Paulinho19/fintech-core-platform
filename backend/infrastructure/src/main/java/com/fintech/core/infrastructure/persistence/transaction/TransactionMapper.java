package com.fintech.core.infrastructure.persistence.transaction;

import com.fintech.core.domain.account.AccountId;
import com.fintech.core.domain.money.Money;
import com.fintech.core.domain.pix.CpfKey;
import com.fintech.core.domain.pix.EmailKey;
import com.fintech.core.domain.pix.EvpKey;
import com.fintech.core.domain.pix.PhoneKey;
import com.fintech.core.domain.pix.PixKey;
import com.fintech.core.domain.transaction.Transaction;
import com.fintech.core.domain.transaction.TransactionId;
import com.fintech.core.domain.transaction.TransactionStatus;
import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionJpaEntity toEntity(Transaction transaction) {
        return new TransactionJpaEntity(
            transaction.id().value(),
            transaction.sourceAccountId().value(),
            keyTypeOf(transaction.targetKey()),
            transaction.targetKey().value(),
            transaction.amount().amount(),
            transaction.amount().currency().getCurrencyCode(),
            transaction.status().name(),
            transaction.createdAt(),
            transaction.settledAt().orElse(null)
        );
    }

    public Transaction toDomain(TransactionJpaEntity entity) {
        Money amount = new Money(entity.getAmount(), Currency.getInstance(entity.getCurrency()));
        return Transaction.reconstitute(
            new TransactionId(entity.getId()),
            new AccountId(entity.getSourceAccountId()),
            toPixKey(entity.getTargetKeyType(), entity.getTargetKeyValue()),
            amount,
            entity.getCreatedAt(),
            TransactionStatus.valueOf(entity.getStatus()),
            entity.getSettledAt()
        );
    }

    // Switch exaustivo sobre a interface sealed PixKey - se um novo tipo de
    // chave for adicionado ao dominio, este metodo para de compilar ate
    // ganhar o case correspondente.
    private String keyTypeOf(PixKey key) {
        return switch (key) {
            case CpfKey ignored -> "CPF";
            case EmailKey ignored -> "EMAIL";
            case PhoneKey ignored -> "PHONE";
            case EvpKey ignored -> "EVP";
        };
    }

    private PixKey toPixKey(String type, String value) {
        return switch (type) {
            case "CPF" -> new CpfKey(value);
            case "EMAIL" -> new EmailKey(value);
            case "PHONE" -> new PhoneKey(value);
            case "EVP" -> new EvpKey(value);
            default -> throw new IllegalStateException("Unknown pix key type persisted: " + type);
        };
    }
}
