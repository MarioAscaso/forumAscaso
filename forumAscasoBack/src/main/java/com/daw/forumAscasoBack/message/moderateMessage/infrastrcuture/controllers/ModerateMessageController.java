package com.daw.forumAscasoBack.message.moderateMessage.infrastrcuture.controllers;

import com.daw.forumAscasoBack.message.moderateMessage.application.ModerateMessageUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            // CORRECCIÓN: Le decimos que busque el estado en la URL (?status=...)
            @RequestParam("status") String status) {

        moderateMessageUseCase.execute(messageId, status);

        return ResponseEntity.ok().build();
    }
}