package com.daw.forumAscasoBack.user.registerParticipant.infrastructure.controllers;

import com.daw.forumAscasoBack.user.registerParticipant.application.RegisterParticipantUseCase;
import com.daw.forumAscasoBack.user.registerParticipant.domain.RegisterParticipantCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class RegisterParticipantController {

    private final RegisterParticipantUseCase useCase;

    public RegisterParticipantController(RegisterParticipantUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterParticipantCommand command) {
        try {
            useCase.execute(command);
            return ResponseEntity.status(HttpStatus.CREATED).body("Participante registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}