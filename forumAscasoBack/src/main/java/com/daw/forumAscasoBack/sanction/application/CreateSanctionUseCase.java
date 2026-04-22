package com.daw.forumAscasoBack.sanction.application;

import com.daw.forumAscasoBack.sanction.domain.CreateSanctionRepositoryPort;
import java.util.Map;

public class CreateSanctionUseCase {
    private final CreateSanctionRepositoryPort repository;

    public CreateSanctionUseCase(CreateSanctionRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(Map<String, Object> data) {
        try {
            if (!data.containsKey("userId") || data.get("userId") == null) {
                throw new IllegalArgumentException("No se ha recibido el ID del usuario a sancionar.");
            }
            Long userId = Long.valueOf(data.get("userId").toString());

            String type = data.containsKey("type") && data.get("type") != null ? data.get("type").toString() : "WARNING";
            String reason = data.containsKey("reason") && data.get("reason") != null ? data.get("reason").toString() : "Sin motivo especificado";
            Integer days = (data.containsKey("days") && data.get("days") != null) ? Integer.valueOf(data.get("days").toString()) : null;

            // SOLUCIÓN: El orden estricto de los parámetros es (Long, String, Integer, String)
            repository.saveSanction(userId, type, days, reason);

        } catch (Exception e) {
            System.err.println("❌ Error al aplicar sanción: " + e.getMessage());
            throw new RuntimeException("Error interno al aplicar la sanción: " + e.getMessage());
        }
    }
}