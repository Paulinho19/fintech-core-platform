package com.fintech.core.domain.exception;

public final class InvalidPixKeyException extends DomainException {

    public InvalidPixKeyException(String type, String value) {
        super("Invalid %s pix key: %s".formatted(type, value));
    }
}
