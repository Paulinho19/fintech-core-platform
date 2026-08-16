-- Suporte ao RF03 (X-Idempotency-Key). Schema criado agora; uso pela
-- aplicacao acontece quando o interceptor de idempotencia for implementado.
CREATE TABLE idempotency_keys (
    idempotency_key VARCHAR(255) PRIMARY KEY,
    request_hash VARCHAR(64) NOT NULL,
    response_status INTEGER,
    response_body TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_idempotency_keys_status CHECK (status IN ('IN_PROGRESS', 'COMPLETED'))
);

CREATE INDEX idx_idempotency_keys_expires_at ON idempotency_keys (expires_at);
