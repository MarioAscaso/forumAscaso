package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.room.createRoom.application.CreateRoomUseCase;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomModuleConfig {

    @Bean
    public CreateRoomUseCase createRoomUseCase(CreateRoomRepositoryPort repository) {
        return new CreateRoomUseCase(repository);
    }
}