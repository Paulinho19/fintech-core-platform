package com.fintech.core.domain.pix;

/**
 * Chave Pix. Sealed para forcar o compilador a exigir tratamento de todos
 * os 4 tipos possiveis em qualquer switch/pattern matching sobre PixKey -
 * se um novo tipo de chave for adicionado, todo switch exaustivo quebra a
 * compilacao ate ser atualizado.
 */
public sealed interface PixKey permits CpfKey, EmailKey, PhoneKey, EvpKey {

    String value();
}
