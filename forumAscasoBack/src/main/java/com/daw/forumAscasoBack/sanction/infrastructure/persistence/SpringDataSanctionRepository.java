package com.daw.forumAscasoBack.sanction.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataSanctionRepository extends JpaRepository<SanctionJpaEntity, Long> {
    List<SanctionJpaEntity> findByUser_Id(Long userId);
}