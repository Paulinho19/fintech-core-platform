CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    currency VARCHAR(3) NOT NULL,
    balance_amount NUMERIC(19, 2) NOT NULL,
    daily_transfer_limit_amount NUMERIC(19, 2) NOT NULL,
    daily_transferred_amount NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_accounts_balance_non_negative CHECK (balance_amount >= 0),
    CONSTRAINT chk_accounts_daily_transferred_non_negative CHECK (daily_transferred_amount >= 0)
);
