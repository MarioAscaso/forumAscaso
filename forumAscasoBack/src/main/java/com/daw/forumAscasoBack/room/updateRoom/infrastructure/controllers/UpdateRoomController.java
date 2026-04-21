package com.daw.forumAscasoBack.room.updateRoom.infrastructure.controllers;

import com.daw.forumAscasoBack.room.updateRoom.application.UpdateRoomUseCase;
import com.daw.forumAscasoBack.room.updateRoom.domain.UpdateRoomCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class UpdateRoomController {

    private final UpdateRoomUseCase useCase;

    public UpdateRoomController(UpdateRoomUseCase useCase) {
        this.useCase = useCase;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRoom(@PathVariable Long id, @RequestBody UpdateRoomCommand command) {
        // Aseguramos que el ID del path sea el que se usa
        UpdateRoomCommand commandWithId = new UpdateRoomCommand(
                id, command.name(), command.description(), command.isUnderModeration()
        );
        useCase.execute(commandWithId);
        return ResponseEntity.ok("Sala actualizada con éxito");
    }
}