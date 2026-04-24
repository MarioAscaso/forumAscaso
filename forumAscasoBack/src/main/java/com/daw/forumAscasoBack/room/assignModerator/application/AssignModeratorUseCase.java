package com.daw.forumAscasoBack.room.assignModerator.application;

import com.daw.forumAscasoBack.room.assignModerator.domain.AssignModeratorRepositoryPort;

public class AssignModeratorUseCase {
    private final AssignModeratorRepositoryPort repository;

    public AssignModeratorUseCase(AssignModeratorRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(Long roomId, Long moderatorId) {
        long count = repository.countRoomsByModeratorId(moderatorId);
        if (count >= 2) throw new RuntimeException("Este moderador ya tiene 2 salas asignadas.");

        repository.assignModeratorToRoom(roomId, moderatorId);

        // CUMPLIMOS EL REQUISITO: Al asignar sala, su rol pasa a ser Moderador
        repository.updateUserRole(moderatorId, "MODERATOR");
    }
}