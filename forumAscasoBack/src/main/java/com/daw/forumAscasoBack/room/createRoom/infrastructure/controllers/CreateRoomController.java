package com.daw.forumAscasoBack.room.createRoom.infrastructure.controllers;

import com.daw.forumAscasoBack.room.createRoom.application.CreateRoomUseCase;
import com.daw.forumAscasoBack.room.createRoom.domain.CreateRoomCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class CreateRoomController {

    private final CreateRoomUseCase createRoomUseCase;

    public CreateRoomController(CreateRoomUseCase createRoomUseCase) {
        this.createRoomUseCase = createRoomUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody CreateRoomCommand command) {
        createRoomUseCase.execute(command);
        return ResponseEntity.ok().build();
    }
}