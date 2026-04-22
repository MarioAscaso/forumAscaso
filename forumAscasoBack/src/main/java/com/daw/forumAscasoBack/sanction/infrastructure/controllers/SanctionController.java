package com.daw.forumAscasoBack.sanction.infrastructure.controllers;

import com.daw.forumAscasoBack.sanction.application.CreateSanctionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/sanctions")
public class SanctionController {

    private final CreateSanctionUseCase createSanctionUseCase;

    public SanctionController(CreateSanctionUseCase createSanctionUseCase) {
        this.createSanctionUseCase = createSanctionUseCase;
    }

    @PostMapping
    public ResponseEntity<String> applySanction(@RequestBody Map<String, Object> body) {
        // Recibimos: userId, roomId (opcional), type (WARNING/BAN), reason, durationInDays
        createSanctionUseCase.execute(body);
        return ResponseEntity.ok("Sanción aplicada correctamente");
    }
}