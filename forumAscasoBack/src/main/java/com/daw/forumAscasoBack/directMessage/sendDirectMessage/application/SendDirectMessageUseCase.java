package com.daw.forumAscasoBack.directMessage.sendDirectMessage.application;

import com.daw.forumAscasoBack.directMessage.sendDirectMessage.domain.SendDirectMessageRepositoryPort;

public class SendDirectMessageUseCase {

    private final SendDirectMessageRepositoryPort repositoryPort;

    public SendDirectMessageUseCase(SendDirectMessageRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public void execute(String senderEmail, Long receiverId, String content) {
        repositoryPort.sendWarning(senderEmail, receiverId, content);
    }
}