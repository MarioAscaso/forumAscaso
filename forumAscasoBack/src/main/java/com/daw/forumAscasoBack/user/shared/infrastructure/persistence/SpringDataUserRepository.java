package com.daw.forumAscasoBack.user.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional; // ¡Importante!

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    // Añadimos esta línea para que Spring sepa buscar por email
    Optional<UserJpaEntity> findByEmail(String email);
    List<UserJpaEntity> findAll();
}