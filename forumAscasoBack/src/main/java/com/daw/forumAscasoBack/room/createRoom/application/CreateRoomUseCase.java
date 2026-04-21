package com.daw.forumAscasoBack.room.createRoom.application;

import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomCommand;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;

public class CreateRoomUseCase {
    private final CreateRoomRepositoryPort repositoryPort;

    public CreateRoomUseCase(CreateRoomRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public void execute(CreateRoomCommand command) {
        // Creamos la sala con el nuevo boolean que viene del comando
        Room room = new Room(null, command.getName(), command.getDescription(), command.isModerated());
        repositoryPort.save(room);
    }
}