package com.daw.forumAscasoBack.sanction.application;

import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SpringDataSanctionRepository;
import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SanctionJpaEntity;
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
        try {
            // 1. Validar que el ID de usuario exista
            if (!data.containsKey("userId") || data.get("userId") == null) {
                throw new IllegalArgumentException("No se ha recibido el ID del usuario a sancionar.");
            }
            Long userId = Long.valueOf(data.get("userId").toString());

            // 2. Extraer el resto de datos de forma segura
            String type = data.containsKey("type") && data.get("type") != null ? data.get("type").toString() : "WARNING";
            String reason = data.containsKey("reason") && data.get("reason") != null ? data.get("reason").toString() : "Sin motivo especificado";

            Integer days = null;
            if (data.containsKey("days") && data.get("days") != null) {
                days = Integer.valueOf(data.get("days").toString());
            }

            // 3. Calcular fecha de caducidad
            LocalDateTime expiry = (days != null) ? LocalDateTime.now().plusDays(days) : null;

            // 4. Buscar usuario y guardar sanción
            UserJpaEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("El usuario con ID " + userId + " no existe en la base de datos."));

            SanctionJpaEntity sanction = new SanctionJpaEntity(user, null, type, reason, expiry);
            repository.save(sanction);

        } catch (Exception e) {
            // Imprimimos el error real en la consola de Java para poder verlo
            System.err.println("❌ Error al aplicar sanción: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error interno al aplicar la sanción: " + e.getMessage());
        }
    }
}