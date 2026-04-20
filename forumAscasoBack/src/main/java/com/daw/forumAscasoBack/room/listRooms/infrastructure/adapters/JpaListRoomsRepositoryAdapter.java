package com.daw.forumAscasoBack.room.listRooms.infrastructure.adapters;

import com.daw.forumAscasoBack.room.listRooms.domain.ListRoomsRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaListRoomsRepositoryAdapter implements ListRoomsRepositoryPort {

    private final SpringDataRoomRepository jpaRepository;

    public JpaListRoomsRepositoryAdapter(SpringDataRoomRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Room> findAll() {
        // Obtenemos las entidades de la BD y las mapeamos a nuestro Dominio puro
        return jpaRepository.findAll().stream().map(entity -> {
            Room room = new Room();
            room.setId(entity.getId());
            room.setName(entity.getName());
            room.setDescription(entity.getDescription());
            room.setUnderModeration(entity.isUnderModeration());
            return room;
        }).collect(Collectors.toList());
    }
}