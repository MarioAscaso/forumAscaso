package com.daw.forumAscasoBack.room.createRoom.domain;

import com.daw.forumAscasoBack.room.shared.domain.model.Room;

public interface CreateRoomRepositoryPort {
    boolean existsByName(String name);
    void save(Room room);
}