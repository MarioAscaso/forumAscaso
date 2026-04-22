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
        List<UserJpaEntity> entities = jpaRepository.findAll();

        return entities.stream().map(entity -> {
            User user = new User();
            user.setId(entity.getId());
            user.setEmail(entity.getEmail());
            user.setUsername(entity.getUsername());

            // CORRECCIÓN: Convertir el String de la BD al Enum User.Role
            if (entity.getRole() != null) {
                try {
                    user.setRole(User.Role.valueOf(entity.getRole()));
                } catch (IllegalArgumentException e) {
                    System.err.println("Rol no reconocido en BD: " + entity.getRole());
                }
            }

            return user;
        }).collect(Collectors.toList());
    }
}