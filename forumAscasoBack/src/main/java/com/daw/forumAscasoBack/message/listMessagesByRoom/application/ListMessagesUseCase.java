package com.daw.forumAscasoBack.message.listMessagesByRoom.application;

import com.daw.forumAscasoBack.message.listMessagesByRoom.domain.ListMessagesRepositoryPort;
import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import java.util.List;

public class ListMessagesUseCase {
    private final ListMessagesRepositoryPort repositoryPort;

    public ListMessagesUseCase(ListMessagesRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public List<Message> execute(Long roomId) {
        return repositoryPort.findByRoomId(roomId);
    }
}