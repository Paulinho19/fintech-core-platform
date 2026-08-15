# Fintech Core Platform 💰

Core Banking / Gateway de Pagamentos Pix simulado, construído com arquitetura hexagonal + DDD.

## Stack

- **Backend:** Java 21 (LTS) + Spring Boot 4.1.0, Maven multi-módulo
- **Arquitetura:** Hexagonal (Ports & Adapters) + DDD + Transactional Outbox
- **Persistência:** PostgreSQL (transacional), MongoDB (auditoria), Redis (cache/lock/rate-limit)
- **Mensageria:** Apache Kafka (KRaft, sem Zookeeper)
- **Frontend:** Angular 21 (ainda não iniciado — Etapa 11)

## Estrutura do repositório

```
fintech-core-platform/
├── backend/                  ← abrir no IntelliJ
│   ├── pom.xml                (parent Maven, packaging=pom)
│   ├── domain/                 Java puro — RNF01: zero dependência de Spring/JPA
│   ├── application/            ports (in/out) + use cases
│   ├── infrastructure/         adapters: JPA, Kafka, Redis, Mongo, REST, Security
│   └── bootstrap/               @SpringBootApplication + application.yml
├── frontend/                 ← abrir no VS Code (ainda não criado)
└── infra/
    └── docker-compose.yml     postgres, mongo, redis, kafka, kafka-ui
```

Por que 4 módulos separados: o RNF01 ("domínio não pode ter anotação de Spring/JPA") é garantido em **tempo de compilação** — `domain/pom.xml` simplesmente não declara essas dependências, então o código nem compila se alguém tentar usá-las ali.

## Como rodar localmente

1. Subir a infra:
   ```
   docker compose -f infra/docker-compose.yml up -d
   ```
2. Abrir `backend/` no IntelliJ (importa os 4 módulos automaticamente via Maven).
3. Rodar `com.fintech.core.bootstrap.FintechCoreApplication`.
4. Validar: `http://localhost:8080/actuator/health` → `{"status":"UP"}`.

Build via linha de comando:
```
cd backend
./mvnw clean install
```

## Status atual

Esqueleto multi-módulo funcional: aplicação Spring Boot sobe conectada a Postgres e Redis, Virtual Threads habilitadas, infra local via Docker Compose definida.

**Ainda não implementado:**
- Domínio (`domain/`) e casos de uso (`application/`) estão vazios — só a estrutura que os vai hospedar
- Sem adapters REST/JPA/Kafka escritos em `infrastructure/`
- Sem Flyway migrations, sem Security/JWT configurado, sem frontend

## Decisões de arquitetura registradas

- **Spring Boot 4.1.0** em vez de 3.x: `start.spring.io` não oferece mais 3.x nas opções padrão no momento em que o projeto foi iniciado. Implica pacotes de autoconfiguração renomeados/modularizados (ex: `spring-boot-starter-web` → `spring-boot-starter-webmvc`).
- **Redisson `4.7.0`**: a versão `3.40.2` (era Boot 3) referenciava a classe `org.springframework.boot.autoconfigure.data.redis.RedisProperties`, que no Boot 4 foi renomeada para `DataRedisProperties` e movida de pacote — causava `ClassNotFoundException` no boot da aplicação. Corrigido subindo para a linha 4.x do Redisson, compatível com Boot 4.
- **JDK 21 LTS** instalado ao lado do JDK 26 (default do sistema) — projeto aponta explicitamente para o 21 via Maven/wrapper, sem alterar `JAVA_HOME` global.
