package com.daw.forumAscasoBack.room.deleteRoom.domain;

public interface DeleteRoomRepositoryPort {
    boolean existsById(Long id);
    void deleteById(Long id);
}