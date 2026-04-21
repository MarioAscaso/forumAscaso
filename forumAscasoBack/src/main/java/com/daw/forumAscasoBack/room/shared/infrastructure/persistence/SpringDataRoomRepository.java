package com.daw.forumAscasoBack.room.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataRoomRepository extends JpaRepository<RoomJpaEntity, Long> {

    long countByModeratorId(Long moderatorId);
}