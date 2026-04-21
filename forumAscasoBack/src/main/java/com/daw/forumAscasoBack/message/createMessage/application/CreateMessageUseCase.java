package com.daw.forumAscasoBack.message.createMessage.application;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;

public class CreateMessageUseCase {
    private final CreateMessageRepositoryPort repositoryPort;

    public CreateMessageUseCase(CreateMessageRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public void execute(Long roomId, String authorEmail, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        repositoryPort.saveMessage(roomId, authorEmail, content);
    }
}