package com.daw.forumAscasoBack.message.createMessage.infrastructure.controllers;

import com.daw.forumAscasoBack.message.createMessage.application.CreateMessageUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class CreateMessageController {

    private final CreateMessageUseCase createMessageUseCase;

    public CreateMessageController(CreateMessageUseCase createMessageUseCase) {
        this.createMessageUseCase = createMessageUseCase;
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<Void> createMessage(
            @PathVariable Long roomId,
            @RequestBody Map<String, String> body,
            Principal principal) {

        // El "principal.getName()" nos devuelve el email guardado en el JWT
        String authorEmail = principal.getName();
        String content = body.get("content");

        createMessageUseCase.execute(roomId, authorEmail, content);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}