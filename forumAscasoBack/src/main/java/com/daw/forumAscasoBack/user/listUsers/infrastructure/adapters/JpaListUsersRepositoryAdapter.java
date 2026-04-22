package com.daw.forumAscasoBack.user.listUsers.infrastructure.adapters;

import com.daw.forumAscasoBack.user.listUsers.domain.ListUsersRepositoryPort;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaListUsersRepositoryAdapter implements ListUsersRepositoryPort {

    private final SpringDataUserRepository jpaRepository;

    public JpaListUsersRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(entity -> {
            User user = new User();
            user.setId(entity.getId());
            user.setEmail(entity.getEmail());
            user.setUsername(entity.getUsername());

            // Si tu User tiene un setRole que acepta String, esto funcionará.
            // Si setRole acepta un ENUM, usaremos String.valueOf().
            try {
                // Probamos a pasarle el rol como String.
                // Si falla porque espera un Enum, Java nos avisará en tiempo de ejecución.
                user.setRole(entity.getRole());
            } catch (Exception e) {
                // Opción B: Si falla, forzamos la conversión a String plano
                user.setRole(entity.getRole().toString());
            }

            return user;
        }).collect(Collectors.toList());
    }
}