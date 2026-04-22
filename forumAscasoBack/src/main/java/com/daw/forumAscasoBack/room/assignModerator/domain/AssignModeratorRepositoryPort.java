package com.daw.forumAscasoBack.room.assignModerator.domain;

public interface AssignModeratorRepositoryPort {
    long countRoomsByModeratorId(Long moderatorId);
    void assignModeratorToRoom(Long roomId, Long moderatorId);
}