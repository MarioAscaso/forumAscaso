package com.daw.forumAscasoBack.room.assignModerator.infrastructure.controllers;

import com.daw.forumAscasoBack.room.assignModerator.application.AssignModeratorUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class AssignModeratorController {

    private final AssignModeratorUseCase assignModeratorUseCase;

    public AssignModeratorController(AssignModeratorUseCase assignModeratorUseCase) {
        this.assignModeratorUseCase = assignModeratorUseCase;
    }

    @PatchMapping("/{roomId}/assign-moderator")
    public ResponseEntity<String> assignModerator(
            @PathVariable Long roomId,
            @RequestBody Map<String, Long> body) {

        Long moderatorId = body.get("moderatorId");
        try {
            assignModeratorUseCase.execute(roomId, moderatorId);
            return ResponseEntity.ok("Moderador asignado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}