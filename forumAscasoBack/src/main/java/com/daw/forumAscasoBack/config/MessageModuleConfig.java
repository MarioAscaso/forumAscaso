package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.message.createMessage.application.CreateMessageUseCase;
import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.listMessagesByRoom.application.ListMessagesUseCase;
import com.daw.forumAscasoBack.message.listMessagesByRoom.domain.ListMessagesRepositoryPort;
import com.daw.forumAscasoBack.message.moderateMessage.application.ModerateMessageUseCase;
import com.daw.forumAscasoBack.message.moderateMessage.domain.ModerateMessageRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageModuleConfig {

    @Bean
    public ListMessagesUseCase listMessagesUseCase(ListMessagesRepositoryPort repositoryPort) {
        return new ListMessagesUseCase(repositoryPort);
    }

    @Bean
    public CreateMessageUseCase createMessageUseCase(CreateMessageRepositoryPort repositoryPort) {
        return new CreateMessageUseCase(repositoryPort);
    }

    @Bean
    public ModerateMessageUseCase moderateMessageUseCase(ModerateMessageRepositoryPort repositoryPort) {
        return new ModerateMessageUseCase(repositoryPort);
    }
}