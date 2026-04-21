package com.daw.forumAscasoBack.room.deleteRoom.infrastructure.adapters;
import com.daw.forumAscasoBack.room.deleteRoom.domain.DeleteRoomRepositoryPort;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaDeleteRoomRepositoryAdapter implements DeleteRoomRepositoryPort {
    private final SpringDataRoomRepository jpaRepository;
    public JpaDeleteRoomRepositoryAdapter(SpringDataRoomRepository jpaRepository) { this.jpaRepository = jpaRepository; }

    @Override public boolean existsById(Long id) { return jpaRepository.existsById(id); }
    @Override public void deleteById(Long id) { jpaRepository.deleteById(id); }
}