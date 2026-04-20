package com.daw.forumAscasoBack.room.listRooms.application;

import com.daw.forumAscasoBack.room.listRooms.domain.ListRoomsRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import java.util.List;

public class ListRoomsUseCase {

    private final ListRoomsRepositoryPort repository;

    public ListRoomsUseCase(ListRoomsRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Room> execute() {
        return repository.findAll();
    }
}