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

Esqueleto multi-módulo funcional: aplicação Spring Boot sobe conectada a Postgres e Redis, Virtual Threads habilitadas, infra local via Docker Compose definida.

**Ainda não implementado:**
- Domínio (`domain/`) e casos de uso (`application/`) estão vazios — só a estrutura que os vai hospedar
- Sem adapters REST/JPA/Kafka escritos em `infrastructure/`
- Sem Flyway migrations, sem Security/JWT configurado, sem frontend
