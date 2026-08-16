package com.fintech.core.domain;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/**
 * Prova automatizada do RNF01: nenhuma classe de dominio pode depender de
 * Spring ou JPA. domain/pom.xml ja garante isso ao nao declarar essas
 * dependencias no classpath principal (o codigo nem compilaria) - este
 * teste e uma segunda camada de defesa que tambem cobre reflection/strings,
 * e documenta a regra de forma executavel.
 */
class DomainPurityArchTest {

    // Inicializado no carregamento da classe - evita metodo @BeforeAll que
    // analisadores estaticos sem suporte a JUnit5 apontam como "nao usado"
    // (a chamada acontece via reflection do JUnit, nao no codigo visivel).
    private static final JavaClasses DOMAIN_CLASSES = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.fintech.core.domain");

    @Test
    void domainMustNotDependOnSpring() {
        noClasses().that().resideInAPackage("com.fintech.core.domain..")
            .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
            .check(DOMAIN_CLASSES);
    }

    @Test
    void domainMustNotDependOnJpa() {
        noClasses().that().resideInAPackage("com.fintech.core.domain..")
            .should().dependOnClassesThat().resideInAnyPackage("jakarta.persistence..")
            .check(DOMAIN_CLASSES);
    }
}
