package com.daw.forumAscasoBack.room.listRooms.infrastructure.adapters;

import com.daw.forumAscasoBack.room.listRooms.domain.ListRoomsRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaListRoomsRepositoryAdapter implements ListRoomsRepositoryPort {

    // 1. Declaramos la variable del repositorio
    private final SpringDataRoomRepository roomRepository;

    // 2. La inyectamos en el constructor
    public JpaListRoomsRepositoryAdapter(SpringDataRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // 3. El método con el nuevo campo isModerated()
    @Override
    public List<Room> findAll() {
        return roomRepository.findAll().stream()
                .map(entity -> new Room(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.isModerated() // <--- El nuevo campo añadido
                ))
                .collect(Collectors.toList());
    }
}