package com.daw.forumAscasoBack.notification.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataNotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {
    // Te servirá más adelante para que el usuario pueda ver sus alertas en el frontend
    List<NotificationJpaEntity> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}