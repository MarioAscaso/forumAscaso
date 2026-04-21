package com.daw.forumAscasoBack.message.moderateMessage.application;

import com.daw.forumAscasoBack.message.moderateMessage.domain.ModerateMessageRepositoryPort;

public class ModerateMessageUseCase {
    private final ModerateMessageRepositoryPort repositoryPort;

    public ModerateMessageUseCase(ModerateMessageRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public void execute(Long messageId, String newStatus) {
        // Validación básica de estados permitidos
        if (!newStatus.equals("APPROVED") && !newStatus.equals("REJECTED")) {
            throw new IllegalArgumentException("Estado no válido. Use APPROVED o REJECTED.");
        }
        repositoryPort.updateMessageStatus(messageId, newStatus);
    }
}