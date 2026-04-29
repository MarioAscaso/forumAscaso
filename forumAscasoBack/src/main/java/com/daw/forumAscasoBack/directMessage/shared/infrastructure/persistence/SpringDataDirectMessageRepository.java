package com.daw.forumAscasoBack.directMessage.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataDirectMessageRepository extends JpaRepository<DirectMessageJpaEntity, Long> {
    List<DirectMessageJpaEntity> findByReceiverId(Long receiverId);
    List<DirectMessageJpaEntity> findBySenderId(Long senderId);
    List<DirectMessageJpaEntity> findAllByOrderBySentAtDesc();
}