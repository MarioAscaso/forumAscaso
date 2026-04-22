package com.daw.forumAscasoBack.sanction.infrastructure.adapters;

import com.daw.forumAscasoBack.sanction.domain.CreateSanctionRepositoryPort;
import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SanctionJpaEntity;
import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SpringDataSanctionRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class JpaCreateSanctionRepositoryAdapter implements CreateSanctionRepositoryPort {

    private final SpringDataSanctionRepository sanctionRepository;
    private final SpringDataUserRepository userRepository;

    public JpaCreateSanctionRepositoryAdapter(SpringDataSanctionRepository sanctionRepository, SpringDataUserRepository userRepository) {
        this.sanctionRepository = sanctionRepository;
        this.userRepository = userRepository;
    }

    // --- AQUÍ ESTÁ LA CORRECCIÓN DEL ORDEN DE PARÁMETROS ---
    @Override
    public void saveSanction(Long userId, String type, Integer days, String reason) {
        UserJpaEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("El usuario con ID " + userId + " no existe."));

        LocalDateTime expiry = (days != null) ? LocalDateTime.now().plusDays(days) : null;

        SanctionJpaEntity sanction = new SanctionJpaEntity(user, null, type, reason, expiry);
        sanctionRepository.save(sanction);
    }
}