package com.fintech.core.domain.pix;

import com.fintech.core.domain.exception.InvalidPixKeyException;
import java.util.regex.Pattern;

public record EmailKey(String value) implements PixKey {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public EmailKey {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidPixKeyException("EMAIL", value);
        }
        value = value.toLowerCase();
    }
}
