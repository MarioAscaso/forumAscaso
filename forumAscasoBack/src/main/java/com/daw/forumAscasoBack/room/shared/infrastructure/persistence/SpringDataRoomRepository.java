package com.daw.forumAscasoBack.room.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRoomRepository extends JpaRepository<RoomJpaEntity, Long> {
    boolean existsByName(String name);
}