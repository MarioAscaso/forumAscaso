package com.daw.forumAscasoBack.user.listUsers.infrastructure.adapters;

import com.daw.forumAscasoBack.user.listUsers.domain.ListUsersRepositoryPort;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaListUsersRepositoryAdapter implements ListUsersRepositoryPort {
    private final SpringDataUserRepository userRepository;

    public JpaListUsersRepositoryAdapter(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(entity -> {
                    User user = new User();
                    user.setId(entity.getId());
                    user.setEmail(entity.getEmail());
                    user.setUsername(entity.getUsername());
                    user.setBanUntil(entity.getBanUntil());
                    try {
                        user.setRole(User.Role.valueOf(entity.getRole().trim().toUpperCase()));
                    } catch (Exception e) {
                        user.setRole(User.Role.PARTICIPANT);
                    }
                    return user;
                })
                .collect(Collectors.toList());
    }
}