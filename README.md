# Fintech Core Platform 💰

Core Banking / Gateway de Pagamentos Pix simulado, construído com arquitetura hexagonal + DDD.

## Stack

- **Backend:** Java 21 (LTS) + Spring Boot 4.1.0, Maven multi-módulo
- **Arquitetura:** Hexagonal (Ports & Adapters) + DDD + Transactional Outbox
- **Persistência:** PostgreSQL (transacional), MongoDB (auditoria), Redis (cache/lock/rate-limit)
- **Mensageria:** Apache Kafka (KRaft, sem Zookeeper)
- **Frontend:** Angular 21

## Estrutura do repositório

```
fintech-core-platform/
├── backend/
│   ├── pom.xml                (parent Maven, packaging=pom)
│   ├── domain/                 Java puro — zero dependência de Spring/JPA
│   ├── application/            ports (in/out) + use cases
│   ├── infrastructure/         adapters: JPA, Kafka, Redis, Mongo, REST, Security
│   └── bootstrap/               @SpringBootApplication + application.yml
├── frontend/
└── infra/
    └── docker-compose.yml     postgres, mongo, redis, kafka, kafka-ui
```

O domínio não pode depender de Spring ou JPA. Isso é garantido em **tempo de compilação**: `domain/pom.xml` simplesmente não declara essas dependências, então o código nem compila se alguém tentar usá-las ali.

## Domínio

Modelado em `domain/` como código Java puro (sem framework):

- **Value objects:** `Money` (quantia + moeda, sempre não-negativa), `PixKey` (sealed: `CpfKey`, `EmailKey`, `PhoneKey`, `EvpKey`, cada um validando seu próprio formato)
- **Agregados:** `Account` (saldo e limite diário de transferência, únicas operações válidas são `debit()`/`credit()`), `Transaction` (máquina de estados: `PENDING → COMPLETED/FAILED`, `COMPLETED → REVERSED`)
- **Resultado de operação:** `TransferResult` (sealed: `Success`/`Failure`), consumido via pattern matching exaustivo em vez de exceções para falhas de negócio esperadas
- **Eventos de domínio:** `PixTransferCompleted`, `PixTransferFailed`

Um teste de arquitetura (`DomainPurityArchTest`) verifica automaticamente que nenhuma classe em `domain/` depende de Spring ou JPA.

## Persistência

`Account` e `Transaction` são persistidos via JPA/Postgres em `infrastructure/`, seguindo o padrão de portas e adapters:

- `application/port/out/`: interfaces `AccountRepository`, `TransactionRepository` — o que a aplicação precisa, sem saber que é Postgres
- `infrastructure/persistence/`: entidades JPA, mappers entidade↔agregado, e os adapters que implementam as interfaces acima usando Spring Data JPA
- Schema versionado via Flyway (`db/migration/`), validado pelo Hibernate na inicialização (`ddl-auto: validate` — nunca gera schema automaticamente)

## Como rodar localmente

1. Subir a infra:
   ```
   docker compose -f infra/docker-compose.yml up -d
   ```
2. Build e execução:
   ```
   cd backend
   ./mvnw clean install
   ./mvnw -pl bootstrap spring-boot:run
   ```
3. Validar: `http://localhost:8080/actuator/health` → `{"status":"UP"}`.

## Status atual

Domínio e persistência implementados e testados. Caso de uso de transferência (`application/`) ainda não existe. Sem endpoints REST, sem Security/JWT, sem Kafka integrado, sem frontend.
