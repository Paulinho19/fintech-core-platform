package com.fintech.core.domain.exception;

/**
 * Base para todas as excecoes de regra de negocio do dominio. Nunca deve
 * carregar detalhe de infraestrutura (status HTTP, codigo de erro de API) -
 * essa traducao acontece na camada de infrastructure.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
