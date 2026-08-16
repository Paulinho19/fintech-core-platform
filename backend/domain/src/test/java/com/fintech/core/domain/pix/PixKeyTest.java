package com.fintech.core.domain.pix;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fintech.core.domain.exception.InvalidPixKeyException;
import org.junit.jupiter.api.Test;

class PixKeyTest {

    @Test
    void acceptsValidCpf() {
        // CPF valido gerado para teste (digitos verificadores corretos)
        assertThat(new CpfKey("529.982.247-25").value()).isEqualTo("52998224725");
    }

    @Test
    void rejectsCpfWithInvalidCheckDigits() {
        assertThatThrownBy(() -> new CpfKey("111.111.111-11"))
            .isInstanceOf(InvalidPixKeyException.class);
    }

    @Test
    void rejectsCpfWithWrongLength() {
        assertThatThrownBy(() -> new CpfKey("123"))
            .isInstanceOf(InvalidPixKeyException.class);
    }

    @Test
    void acceptsValidEmail() {
        assertThat(new EmailKey("User@Example.com").value()).isEqualTo("user@example.com");
    }

    @Test
    void rejectsInvalidEmail() {
        assertThatThrownBy(() -> new EmailKey("not-an-email"))
            .isInstanceOf(InvalidPixKeyException.class);
    }

    @Test
    void acceptsValidBrazilianPhone() {
        assertThat(new PhoneKey("+5511987654321").value()).isEqualTo("+5511987654321");
    }

    @Test
    void rejectsPhoneWithoutCountryCode() {
        assertThatThrownBy(() -> new PhoneKey("11987654321"))
            .isInstanceOf(InvalidPixKeyException.class);
    }

    @Test
    void acceptsValidEvpKey() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        assertThat(new EvpKey(uuid).value()).isEqualTo(uuid);
    }

    @Test
    void rejectsNonUuidEvpKey() {
        assertThatThrownBy(() -> new EvpKey("not-a-uuid"))
            .isInstanceOf(InvalidPixKeyException.class);
    }
}
