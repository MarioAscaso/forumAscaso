package com.daw.forumAscasoBack.room.listRooms.infrastructure.controllers;

import com.daw.forumAscasoBack.room.listRooms.application.ListRoomsUseCase;
import com.daw.forumAscasoBack.room.shared.domain.model.Room;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ListRoomsController {

    private final ListRoomsUseCase useCase;
    // Añadimos el repositorio para poder buscar salas individuales directamente
    private final SpringDataRoomRepository roomRepository;

    public ListRoomsController(ListRoomsUseCase useCase, SpringDataRoomRepository roomRepository) {
        this.useCase = useCase;
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public ResponseEntity<List<Room>> listRooms() {
        return ResponseEntity.ok(useCase.execute());
    }

    // 🔥 LA SOLUCIÓN: Nuevo endpoint para obtener UNA sola sala por su ID 🔥
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}