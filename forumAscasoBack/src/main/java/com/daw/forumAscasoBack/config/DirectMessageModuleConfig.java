package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.directMessage.sendDirectMessage.application.SendDirectMessageUseCase;
import com.daw.forumAscasoBack.directMessage.sendDirectMessage.domain.SendDirectMessageRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectMessageModuleConfig {

    @Bean
    public SendDirectMessageUseCase sendDirectMessageUseCase(SendDirectMessageRepositoryPort port) {
        return new SendDirectMessageUseCase(port);
    }
}