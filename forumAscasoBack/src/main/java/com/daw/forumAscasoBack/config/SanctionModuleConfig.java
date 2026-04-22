package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.sanction.application.CreateSanctionUseCase;
import com.daw.forumAscasoBack.sanction.domain.CreateSanctionRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SanctionModuleConfig {

    @Bean
    public CreateSanctionUseCase createSanctionUseCase(CreateSanctionRepositoryPort port) {
        return new CreateSanctionUseCase(port);
    }
}