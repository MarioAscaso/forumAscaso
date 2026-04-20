package com.daw.forumAscasoBack.room.listRooms.domain;

import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import java.util.List;

public interface ListRoomsRepositoryPort {
    List<Room> findAll();
}