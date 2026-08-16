package com.fintech.core.domain.pix;

import com.fintech.core.domain.exception.InvalidPixKeyException;

/**
 * Chave Pix do tipo CPF. Valida os digitos verificadores reais do CPF,
 * nao so o formato - um invariante de dominio, nao uma regra de UI.
 */
public record CpfKey(String value) implements PixKey {

    public CpfKey {
        String digits = value == null ? "" : value.replaceAll("\\D", "");
        if (!isValidCpf(digits)) {
            throw new InvalidPixKeyException("CPF", value);
        }
        value = digits;
    }

    private static boolean isValidCpf(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        return checkDigit(cpf, 9) == cpf.charAt(9) - '0'
            && checkDigit(cpf, 10) == cpf.charAt(10) - '0';
    }

    private static int checkDigit(String cpf, int length) {
        int sum = 0;
        int weight = length + 1;
        for (int i = 0; i < length; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
