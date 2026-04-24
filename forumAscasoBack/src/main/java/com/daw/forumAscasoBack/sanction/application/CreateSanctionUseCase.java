package com.daw.forumAscasoBack.sanction.application;

import com.daw.forumAscasoBack.sanction.domain.CreateSanctionRepositoryPort;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import java.time.LocalDateTime;

public class CreateSanctionUseCase {
    private final CreateSanctionRepositoryPort repository;
    private final SpringDataUserRepository userRepository;

    public CreateSanctionUseCase(CreateSanctionRepositoryPort repository, SpringDataUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void execute(Long userId, String type, Integer days, String reason) {
        repository.save(userId, type, days, reason);

        if ("TEMPORARY_BAN".equals(type) && days != null) {
            UserJpaEntity user = userRepository.findById(userId).orElseThrow();
            user.setRole("BANNED");
            user.setBanUntil(LocalDateTime.now().plusDays(days));
            userRepository.save(user);
        }
    }
}