CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    source_account_id UUID NOT NULL REFERENCES accounts (id),
    target_key_type VARCHAR(10) NOT NULL,
    target_key_value VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    settled_at TIMESTAMPTZ,
    CONSTRAINT chk_transactions_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_transactions_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'REVERSED')),
    CONSTRAINT chk_transactions_target_key_type CHECK (target_key_type IN ('CPF', 'EMAIL', 'PHONE', 'EVP'))
);

CREATE INDEX idx_transactions_source_account_id ON transactions (source_account_id);
