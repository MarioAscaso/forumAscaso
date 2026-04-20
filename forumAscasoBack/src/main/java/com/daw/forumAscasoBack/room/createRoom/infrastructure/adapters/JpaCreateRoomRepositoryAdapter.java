package com.daw.forumAscasoBack.room.createRoom.infrastructure.adapters;

import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaCreateRoomRepositoryAdapter implements CreateRoomRepositoryPort {

    private final SpringDataRoomRepository jpaRepository;

    public JpaCreateRoomRepositoryAdapter(SpringDataRoomRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void save(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity();
        entity.setName(room.getName());
        entity.setDescription(room.getDescription());
        entity.setUnderModeration(room.isUnderModeration());

        jpaRepository.save(entity);
    }
}