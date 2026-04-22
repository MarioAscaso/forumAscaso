package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.room.createRoom.application.CreateRoomUseCase;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.deleteRoom.application.DeleteRoomUseCase;
import com.daw.forumAscasoBack.room.deleteRoom.domain.DeleteRoomRepositoryPort;
import com.daw.forumAscasoBack.room.assignModerator.application.AssignModeratorUseCase;
import com.daw.forumAscasoBack.room.assignModerator.domain.AssignModeratorRepositoryPort;
import com.daw.forumAscasoBack.room.listRooms.application.ListRoomsUseCase;
import com.daw.forumAscasoBack.room.listRooms.domain.ListRoomsRepositoryPort;
import com.daw.forumAscasoBack.room.updateRoom.application.UpdateRoomUseCase;
import com.daw.forumAscasoBack.room.updateRoom.domain.UpdateRoomRepositoryPort;
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

    @Bean
    public UpdateRoomUseCase updateRoomUseCase(UpdateRoomRepositoryPort repository) {
        return new UpdateRoomUseCase(repository);
    }

    @Bean
    public DeleteRoomUseCase deleteRoomUseCase(DeleteRoomRepositoryPort repository) {
        return new DeleteRoomUseCase(repository);
    }

    @Bean
    public AssignModeratorUseCase assignModeratorUseCase(AssignModeratorRepositoryPort port) {
        return new AssignModeratorUseCase(port);
    }
}