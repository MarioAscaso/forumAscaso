package com.daw.forumAscasoBack.message.listMessagesByRoom.infrastructure.controllers;

import com.daw.forumAscasoBack.message.listMessagesByRoom.application.ListMessagesUseCase;
import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ListMessagesController {

    private final ListMessagesUseCase listMessagesUseCase;

    public ListMessagesController(ListMessagesUseCase listMessagesUseCase) {
        this.listMessagesUseCase = listMessagesUseCase;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Message>> getMessagesByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(listMessagesUseCase.execute(roomId));
    }

    // NUEVO ENDPOINT:
    @GetMapping("/room/{roomId}/pending")
    public ResponseEntity<List<Message>> getPendingMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(listMessagesUseCase.executePending(roomId));
    }
}