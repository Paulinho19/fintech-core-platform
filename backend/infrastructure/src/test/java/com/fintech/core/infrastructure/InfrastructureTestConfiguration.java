package com.fintech.core.infrastructure;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * "Ancora" que @DataJpaTest usa para achar uma configuracao Spring Boot
 * valida subindo a hierarquia de pacotes a partir da classe de teste. Sem
 * isto, testes em com.fintech.core.infrastructure.persistence.* falhariam
 * com "Unable to find @SpringBootConfiguration" - este modulo nao tem
 * classe @SpringBootApplication (fica em bootstrap), entao a fatia de
 * teste precisa da sua propria.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class InfrastructureTestConfiguration {
}
