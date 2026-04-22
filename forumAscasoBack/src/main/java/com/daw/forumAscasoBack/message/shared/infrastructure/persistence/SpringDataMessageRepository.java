package com.daw.forumAscasoBack.message.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringDataMessageRepository extends JpaRepository<MessageJpaEntity, Long> {

    // Para el muro público
    List<MessageJpaEntity> findByRoom_IdOrderByCreationDateAsc(Long roomId);

    // Para la bandeja de moderación
    List<MessageJpaEntity> findByRoom_IdAndStatusOrderByCreationDateAsc(Long roomId, String status);

    // NUEVO: Para el límite de 10 mensajes por semana (Anti-Spam)
    long countByAuthor_IdAndCreationDateAfter(Long authorId, LocalDateTime date);
}