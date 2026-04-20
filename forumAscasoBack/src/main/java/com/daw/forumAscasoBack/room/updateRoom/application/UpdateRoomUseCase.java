package com.daw.forumAscasoBack.room.updateRoom.application;

import com.daw.forumAscasoBack.room.updateRoom.domain.UpdateRoomCommand;
import com.daw.forumAscasoBack.room.updateRoom.domain.UpdateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;

public class UpdateRoomUseCase {

    // 1. DEBES DECLARAR LA VARIABLE AQUÍ
    private final UpdateRoomRepositoryPort repository;

    // 2. DEBES INYECTARLA EN EL CONSTRUCTOR
    public UpdateRoomUseCase(UpdateRoomRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(UpdateRoomCommand command) {
        // Ahora Java ya sabe qué es 'repository' porque lo declaramos arriba
        RoomJpaEntity entity = repository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));

        entity.setName(command.name());
        entity.setDescription(command.description());
        entity.setUnderModeration(command.isUnderModeration());

        repository.save(entity);
    }
}