package com.fintech.core.domain.pix;

import com.fintech.core.domain.exception.InvalidPixKeyException;
import java.util.regex.Pattern;

/** Formato E.164 para o Brasil: +55DDNNNNNNNNN (10 ou 11 digitos apos o DDI). */
public record PhoneKey(String value) implements PixKey {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+55\\d{10,11}$");

    public PhoneKey {
        if (value == null || !PHONE_PATTERN.matcher(value).matches()) {
            throw new InvalidPixKeyException("PHONE", value);
        }
    }
}
