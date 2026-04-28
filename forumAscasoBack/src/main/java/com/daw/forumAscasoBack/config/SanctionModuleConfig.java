package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.sanction.application.CreateSanctionUseCase;
import com.daw.forumAscasoBack.sanction.domain.CreateSanctionRepositoryPort;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SanctionModuleConfig {

    @Bean
    public CreateSanctionUseCase createSanctionUseCase(
            CreateSanctionRepositoryPort createSanctionRepositoryPort,
            SpringDataUserRepository userRepository) { // ¡Añadimos el nuevo repositorio aquí!

        // Ahora sí le pasamos los 2 parámetros que necesita nuestro Caso de Uso actualizado
        return new CreateSanctionUseCase(createSanctionRepositoryPort, userRepository);
    }
}