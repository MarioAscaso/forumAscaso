package com.daw.forumAscasoBack.room.listRooms.infrastructure.controllers;

import com.daw.forumAscasoBack.room.listRooms.application.ListRoomsUseCase;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ListRoomsController {

    private final ListRoomsUseCase useCase;

    public ListRoomsController(ListRoomsUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<List<Room>> listRooms() {
        return ResponseEntity.ok(useCase.execute());
    }
}