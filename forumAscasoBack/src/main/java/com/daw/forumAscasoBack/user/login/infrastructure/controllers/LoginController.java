package com.daw.forumAscasoBack.user.login.infrastructure.controllers;

import com.daw.forumAscasoBack.user.login.application.LoginUseCase;
import com.daw.forumAscasoBack.user.login.domain.LoginCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class LoginController {

    private final LoginUseCase useCase;

    public LoginController(LoginUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginCommand command) {
        try {
            String token = useCase.execute(command);
            return ResponseEntity.ok(token); // Devuelve el Token 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); // 401 Credenciales inválidas
        }
    }
}