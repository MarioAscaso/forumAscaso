package com.daw.forumAscasoBack.room.createRoom.infrastructure.adapters;

import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaCreateRoomRepositoryAdapter implements CreateRoomRepositoryPort {

    private final SpringDataRoomRepository roomRepository;

    public JpaCreateRoomRepositoryAdapter(SpringDataRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void save(Room room) {
        // Usamos el constructor actualizado que creamos en el paso anterior
        RoomJpaEntity entity = new RoomJpaEntity(room.getName(), room.getDescription(), room.isModerated());
        roomRepository.save(entity);
    }
}