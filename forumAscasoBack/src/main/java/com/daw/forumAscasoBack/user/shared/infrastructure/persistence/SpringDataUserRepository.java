package com.daw.forumAscasoBack.user.shared.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {

    // Para buscar al usuario en el login y en la creación de mensajes
    Optional<UserJpaEntity> findByEmail(String email);

    // Para comprobar si el correo ya está registrado
    boolean existsByEmail(String email);

    // Para comprobar si el nombre de usuario ya está registrado (¡EL QUE FALTABA!)
    boolean existsByUsername(String username);

    // Para el nuevo sistema de menciones (@username)
    Optional<UserJpaEntity> findByUsername(String username);
}