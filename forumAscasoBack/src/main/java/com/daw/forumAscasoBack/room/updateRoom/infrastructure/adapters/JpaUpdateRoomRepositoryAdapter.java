package com.daw.forumAscasoBack.room.updateRoom.infrastructure.adapters;
import com.daw.forumAscasoBack.room.updateRoom.domain.UpdateRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class JpaUpdateRoomRepositoryAdapter implements UpdateRoomRepositoryPort {
    private final SpringDataRoomRepository jpaRepository;
    public JpaUpdateRoomRepositoryAdapter(SpringDataRoomRepository jpaRepository) { this.jpaRepository = jpaRepository; }

    @Override public Optional<RoomJpaEntity> findById(Long id) { return jpaRepository.findById(id); }
    @Override public void save(RoomJpaEntity room) { jpaRepository.save(room); }
}