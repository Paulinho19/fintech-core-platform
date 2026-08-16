package com.fintech.core.domain.transaction;

/**
 * Resultado de uma tentativa de transferencia. Sealed + records em vez de
 * lancar excecao para falhas de negocio esperadas (saldo insuficiente,
 * limite excedido) - forca quem consome o resultado a tratar sucesso e
 * falha explicitamente via pattern matching exaustivo, sem try/catch.
 *
 * Exemplo de uso (Java 21 pattern matching for switch):
 *
 *   String message = switch (result) {
 *       case TransferResult.Success s -> "OK: " + s.transaction().id();
 *       case TransferResult.Failure f -> "Erro: " + f.reason();
 *   };
 */
public sealed interface TransferResult {

    record Success(Transaction transaction) implements TransferResult {
    }

    record Failure(FailureReason reason, String message) implements TransferResult {
    }
}
