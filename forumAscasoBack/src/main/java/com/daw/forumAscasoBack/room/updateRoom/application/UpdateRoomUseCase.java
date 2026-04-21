package com.daw.forumAscasoBack.room.updateRoom.application;
import com.daw.forumAscasoBack.room.updateRoom.domain.*;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;

public class UpdateRoomUseCase {
    private final UpdateRoomRepositoryPort repository;
    public UpdateRoomUseCase(UpdateRoomRepositoryPort repository) { this.repository = repository; }

    public void execute(UpdateRoomCommand command) {
        RoomJpaEntity entity = repository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada"));
        entity.setName(command.name());
        entity.setDescription(command.description());
        entity.setUnderModeration(command.isUnderModeration());
        repository.save(entity);
    }
}