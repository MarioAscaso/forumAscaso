package com.daw.forumAscasoBack.notification.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataNotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {
}