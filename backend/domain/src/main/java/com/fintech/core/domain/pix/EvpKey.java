package com.fintech.core.domain.pix;

import com.fintech.core.domain.exception.InvalidPixKeyException;
import java.util.UUID;

/** Chave aleatoria (EVP - Endereçamento Virtual de Pagamento), gerada como UUID. */
public record EvpKey(String value) implements PixKey {

    public EvpKey {
        try {
            value = UUID.fromString(value).toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidPixKeyException("EVP", value);
        }
    }
}
