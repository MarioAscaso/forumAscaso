package com.daw.forumAscasoBack.sanction.infrastructure.controllers;

import com.daw.forumAscasoBack.sanction.application.CreateSanctionUseCase;
import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SpringDataSanctionRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sanctions")
public class SanctionController {

    private final CreateSanctionUseCase createSanctionUseCase;
    private final SpringDataUserRepository userRepository;
    private final SpringDataSanctionRepository sanctionRepository;

    public SanctionController(CreateSanctionUseCase createSanctionUseCase,
                              SpringDataUserRepository userRepository,
                              SpringDataSanctionRepository sanctionRepository) {
        this.createSanctionUseCase = createSanctionUseCase;
        this.userRepository = userRepository;
        this.sanctionRepository = sanctionRepository;
    }

    @PostMapping
    public ResponseEntity<String> applySanction(@RequestBody Map<String, Object> body) {
        try {
            // 1. Extraemos los datos del JSON recibido (desempaquetado seguro)
            Long userId = Long.valueOf(body.get("userId").toString());
            String type = (String) body.get("type");
            Integer days = body.get("days") != null ? Integer.valueOf(body.get("days").toString()) : null;
            String reason = (String) body.get("reason");

            // 2. Ahora sí, le pasamos los 4 parámetros exactos que espera nuestro UseCase
            createSanctionUseCase.execute(userId, type, days, reason);

            return ResponseEntity.ok("Sanción aplicada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-sanctions")
    public ResponseEntity<?> getMySanctions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(sanctionRepository.findByUser_Id(user.getId())))
                .orElse(ResponseEntity.notFound().build());
    }
}