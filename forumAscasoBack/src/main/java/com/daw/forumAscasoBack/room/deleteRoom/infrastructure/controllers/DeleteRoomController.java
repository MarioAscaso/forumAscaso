package com.daw.forumAscasoBack.room.deleteRoom.infrastructure.controllers;

// ¡EL IMPORT CORRECTO!
import com.daw.forumAscasoBack.room.deleteRoom.application.DeleteRoomUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class DeleteRoomController {

    private final DeleteRoomUseCase useCase;

    public DeleteRoomController(DeleteRoomUseCase useCase) {
        this.useCase = useCase;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        useCase.execute(id);
        return ResponseEntity.ok("Sala eliminada con éxito");
    }
}