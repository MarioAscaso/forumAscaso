package com.daw.forumAscasoBack.room.createRoom.application;

import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomCommand;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;

public class CreateRoomUseCase {

    private final CreateRoomRepositoryPort repository;

    public CreateRoomUseCase(CreateRoomRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(CreateRoomCommand command) {
        // 1. Validamos que no exista ya una sala con ese nombre
        if (repository.existsByName(command.name())) {
            throw new IllegalArgumentException("Ya existe una sala con ese nombre");
        }

        // 2. Mapeamos el comando al modelo de dominio puro
        Room newRoom = new Room();
        newRoom.setName(command.name());
        newRoom.setDescription(command.description());
        newRoom.setUnderModeration(command.isUnderModeration());

        // 3. Guardamos
        repository.save(newRoom);
    }
}