package com.daw.forumAscasoBack.directMessage.sendDirectMessage.infrastructure.controllers;

import com.daw.forumAscasoBack.directMessage.sendDirectMessage.application.SendDirectMessageUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/direct-messages")
public class SendDirectMessageController {

    private final SendDirectMessageUseCase sendDirectMessageUseCase;

    public SendDirectMessageController(SendDirectMessageUseCase sendDirectMessageUseCase) {
        this.sendDirectMessageUseCase = sendDirectMessageUseCase;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendWarning(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {

        String senderEmail = authentication.getName();
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String content = payload.get("content").toString();

        sendDirectMessageUseCase.execute(senderEmail, receiverId, content);

        return ResponseEntity.ok("Aviso enviado correctamente.");
    }
}