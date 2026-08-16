-- Suporte ao Transactional Outbox Pattern (RNF04). Schema criado agora; o
-- poller que publica no Kafka vem junto com a integracao de mensageria.
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at TIMESTAMPTZ
);

-- Indice parcial: so indexa eventos ainda nao publicados, que e a unica
-- consulta que o poller faz repetidamente. Muito menor e mais rapido que
-- indexar a tabela inteira.
CREATE INDEX idx_outbox_events_unpublished ON outbox_events (created_at) WHERE published_at IS NULL;
