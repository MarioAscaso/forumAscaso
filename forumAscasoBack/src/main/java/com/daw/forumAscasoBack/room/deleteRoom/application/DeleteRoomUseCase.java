package com.daw.forumAscasoBack.room.deleteRoom.application;
import com.daw.forumAscasoBack.room.deleteRoom.domain.DeleteRoomRepositoryPort;

public class DeleteRoomUseCase {
    private final DeleteRoomRepositoryPort repository;
    public DeleteRoomUseCase(DeleteRoomRepositoryPort repository) { this.repository = repository; }

    public void execute(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Sala no encontrada");
        repository.deleteById(id);
    }
}