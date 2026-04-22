package com.daw.forumAscasoBack.sanction.application;

import com.daw.forumAscasoBack.sanction.shared.infrastructure.persistence.SpringDataSanctionRepository;
import com.daw.forumAscasoBack.sanction.shared.infrastructure.persistence.SanctionJpaEntity;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CreateSanctionUseCase {
    private final SpringDataSanctionRepository repository;
    private final SpringDataUserRepository userRepository;

    public CreateSanctionUseCase(SpringDataSanctionRepository repository, SpringDataUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void execute(Map<String, Object> data) {
        Long userId = Long.valueOf(data.get("userId").toString());
        String type = data.get("type").toString();
        String reason = data.get("reason").toString();
        Integer days = data.containsKey("days") ? Integer.valueOf(data.get("days").toString()) : null;

        LocalDateTime expiry = (days != null) ? LocalDateTime.now().plusDays(days) : null;

        // CORRECCIÓN AQUÍ: Cambiamos 'var' por 'UserJpaEntity'
        UserJpaEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SanctionJpaEntity sanction = new SanctionJpaEntity(user, null, type, reason, expiry);
        repository.save(sanction);
    }
}