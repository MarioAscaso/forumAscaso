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

    // Inyectamos todas las dependencias necesarias en el constructor
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
            createSanctionUseCase.execute(body);
            return ResponseEntity.ok("Sanción aplicada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-sanctions")
    public ResponseEntity<?> getMySanctions() {
        // Sacamos el email del usuario que ha hecho la petición usando el Token JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(sanctionRepository.findByUser_Id(user.getId())))
                .orElse(ResponseEntity.notFound().build());
    }
}