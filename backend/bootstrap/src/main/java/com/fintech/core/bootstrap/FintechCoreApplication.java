package com.fintech.core.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada. scanBasePackages aponta para a raiz "com.fintech.core"
 * porque o scan padrao so cobriria o pacote deste classe (com.fintech.core.bootstrap),
 * deixando application/infrastructure de fora.
 */
@SpringBootApplication(scanBasePackages = "com.fintech.core")
public class FintechCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechCoreApplication.class, args);
    }
}
