package com.daw.forumAscasoBack.room.assignModerator.infrastructure.adapters;

import com.daw.forumAscasoBack.room.assignModerator.domain.AssignModeratorRepositoryPort;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaAssignModeratorRepositoryAdapter implements AssignModeratorRepositoryPort {
    private final SpringDataRoomRepository roomRepository;
    private final SpringDataUserRepository userRepository;

    public JpaAssignModeratorRepositoryAdapter(SpringDataRoomRepository roomRepository, SpringDataUserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public long countRoomsByModeratorId(Long moderatorId) {
        return roomRepository.countByModeratorId(moderatorId);
    }

    @Override
    public void assignModeratorToRoom(Long roomId, Long moderatorId) {
        RoomJpaEntity room = roomRepository.findById(roomId).orElseThrow();
        UserJpaEntity moderator = userRepository.findById(moderatorId).orElseThrow();
        room.setModerator(moderator);
        roomRepository.save(room);
    }
}