package com.daw.forumAscasoBack.sanction.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataSanctionRepository extends JpaRepository<SanctionJpaEntity, Long> {
    // Buscar si el usuario tiene algún baneo activo ahora mismo
    List<SanctionJpaEntity> findByUser_IdAndExpiryDateAfter(Long userId, LocalDateTime now);
}