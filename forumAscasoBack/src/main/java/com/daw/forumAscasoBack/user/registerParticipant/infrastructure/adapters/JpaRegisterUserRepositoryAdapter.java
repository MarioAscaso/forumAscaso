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
        entity.setPassword(user.getPassword());

        // Extraemos el nombre del Enum para guardarlo en la Base de Datos como String
        entity.setRole(user.getRole() != null ? user.getRole().name() : User.Role.PARTICIPANT.name());

        jpaRepository.save(entity);
    }
}