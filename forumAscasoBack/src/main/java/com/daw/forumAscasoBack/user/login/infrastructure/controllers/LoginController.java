package com.daw.forumAscasoBack.user.login.infrastructure.controllers;

import com.daw.forumAscasoBack.user.login.application.LoginUseCase;
import com.daw.forumAscasoBack.user.login.domain.LoginCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
// OJO: Cambiamos a /api/auth porque así lo configuramos en el Front (Login.jsx)
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginUseCase useCase;

    public LoginController(LoginUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCommand command) {
        try {
            // Ahora recibimos y devolvemos un Map (que Spring convierte en JSON automáticamente)
            Map<String, String> response = useCase.execute(command);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}