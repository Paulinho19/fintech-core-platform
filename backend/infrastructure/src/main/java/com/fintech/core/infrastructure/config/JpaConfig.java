package com.fintech.core.infrastructure.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Sem isto, entidades JPA e repositorios de Spring Data em
 * com.fintech.core.infrastructure.persistence.* NAO seriam encontrados.
 *
 * @SpringBootApplication(scanBasePackages="com.fintech.core") no bootstrap
 * controla o COMPONENT SCAN (achar @Component/@Repository/@Service), mas o
 * ENTITY SCAN e o REPOSITORY SCAN do Spring Data JPA usam por padrao o
 * pacote de onde a classe @SpringBootApplication esta declarada
 * (com.fintech.core.bootstrap) - nao o scanBasePackages. Sem @EntityScan e
 * @EnableJpaRepositories explicitos apontando pra infrastructure, o
 * Hibernate simplesmente nao veria AccountJpaEntity/TransactionJpaEntity.
 */
@Configuration
@EntityScan(basePackages = "com.fintech.core.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.fintech.core.infrastructure.persistence")
public class JpaConfig {
}
