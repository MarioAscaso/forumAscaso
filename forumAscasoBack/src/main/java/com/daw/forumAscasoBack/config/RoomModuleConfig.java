package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.room.createRoom.application.CreateRoomUseCase;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.listRooms.application.ListRoomsUseCase;
import com.daw.forumAscasoBack.room.listRooms.domain.ListRoomsRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomModuleConfig {

    // Bean para el caso de uso de Crear Sala
    @Bean
    public CreateRoomUseCase createRoomUseCase(CreateRoomRepositoryPort repository) {
        return new CreateRoomUseCase(repository);
    }

    // Bean para el caso de uso de Listar Salas
    @Bean
    public ListRoomsUseCase listRoomsUseCase(ListRoomsRepositoryPort repository) {
        return new ListRoomsUseCase(repository);
    }
}