package com.daw.forumAscasoBack.room.assignModerator.application;

import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Service;

@Service
public class AssignModeratorUseCase {
    private final SpringDataRoomRepository roomRepository;
    private final SpringDataUserRepository userRepository;

    public AssignModeratorUseCase(SpringDataRoomRepository roomRepository, SpringDataUserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public void execute(Long roomId, Long moderatorId) {
        RoomJpaEntity room = roomRepository.findById(roomId).orElseThrow();
        UserJpaEntity moderator = userRepository.findById(moderatorId).orElseThrow();

        // REQUISITO: Máximo 2 salas por moderador
        long count = roomRepository.countByModeratorId(moderatorId);
        if (count >= 2) throw new RuntimeException("Este moderador ya tiene 2 salas asignadas.");

        room.setModerator(moderator);
        roomRepository.save(room);
    }
}