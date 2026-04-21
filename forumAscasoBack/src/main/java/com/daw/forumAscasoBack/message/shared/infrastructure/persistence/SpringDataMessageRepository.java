package com.daw.forumAscasoBack.message.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringDataMessageRepository extends JpaRepository<MessageJpaEntity, Long> {

    // Spring Data crea automáticamente la consulta SQL por el nombre del método:
    // "Encuentra por RoomId y ordénalos por CreationDate de forma ascendente"
    List<MessageJpaEntity> findByRoom_IdOrderByCreationDateAsc(Long roomId);
}