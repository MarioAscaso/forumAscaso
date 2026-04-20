package com.daw.forumAscasoBack.room.createRoom.infrastructure.controllers;

import com.daw.forumAscasoBack.room.createRoom.application.CreateRoomUseCase;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class CreateRoomController {

    private final CreateRoomUseCase useCase;

    public CreateRoomController(CreateRoomUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<String> createRoom(@RequestBody CreateRoomCommand command) {
        try {
            useCase.execute(command);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sala temática creada con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}