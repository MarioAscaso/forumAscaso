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

    // 🔥 CORRECCIÓN: Ahora escucha directamente en /api/messages 🔥
    @PostMapping
    public ResponseEntity<Void> createMessage(
            @RequestBody Map<String, Object> body,
            Principal principal) {

        // El "principal.getName()" nos devuelve el email guardado en el JWT
        String authorEmail = principal.getName();

        // Extraemos el contenido del JSON
        String content = (String) body.get("content");

        // Extraemos el roomId que React envía dentro del JSON
        Long roomId = Long.valueOf(body.get("roomId").toString());

        createMessageUseCase.execute(roomId, authorEmail, content);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}