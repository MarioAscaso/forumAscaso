package com.daw.forumAscasoBack.room.createRoom.domain;

import com.daw.forumAscasoBack.room.shared.domain.model.Room;

public interface CreateRoomRepositoryPort {
    void save(Room room);
}