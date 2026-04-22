package com.daw.forumAscasoBack.user.registerParticipant.infrastructure.adapters;

import com.daw.forumAscasoBack.user.registerParticipant.domain.RegisterUserRepositoryPort;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaRegisterUserRepositoryAdapter implements RegisterUserRepositoryPort {

    private final SpringDataUserRepository jpaRepository;

    public JpaRegisterUserRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public void save(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());

        // CORRECCIÓN 1: Usamos setPassword en lugar de setPasswordHash
        entity.setPassword(user.getPasswordHash());

        // CORRECCIÓN 2: Guardamos el rol como String
        entity.setRole(user.getRole() != null ? user.getRole().toString() : "PARTICIPANT");

        // (Se ha eliminado el 'setStatus' ya que no lo necesitamos en la BD actual)

        jpaRepository.save(entity);
    }
}