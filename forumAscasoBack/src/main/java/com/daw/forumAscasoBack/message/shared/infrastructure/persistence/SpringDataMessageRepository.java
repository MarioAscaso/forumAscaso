package com.daw.forumAscasoBack.message.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataMessageRepository extends JpaRepository<MessageJpaEntity, Long> {

    // NUEVO MÉTODO: Busca por Sala Y por Estado (para filtrar los PENDING o REJECTED)
    List<MessageJpaEntity> findByRoom_IdAndStatusOrderByCreationDateAsc(Long roomId, String status);
}