package com.daw.forumAscasoBack.message.moderateMessage.infrastructure.controllers;

import com.daw.forumAscasoBack.message.moderateMessage.application.ModerateMessageUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class ModerateMessageController {

    private final ModerateMessageUseCase moderateMessageUseCase;

    public ModerateMessageController(ModerateMessageUseCase moderateMessageUseCase) {
        this.moderateMessageUseCase = moderateMessageUseCase;
    }

    @PatchMapping("/{messageId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long messageId,
            @RequestBody Map<String, String> body) {

        String newStatus = body.get("status");
        moderateMessageUseCase.execute(messageId, newStatus);

        return ResponseEntity.ok().build();
    }
}