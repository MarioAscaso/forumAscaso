package com.daw.forumAscasoBack.room.updateRoom.domain;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import java.util.Optional;

public interface UpdateRoomRepositoryPort {
    Optional<RoomJpaEntity> findById(Long id);
    void save(RoomJpaEntity room);
}