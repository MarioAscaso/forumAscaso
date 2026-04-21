package com.daw.forumAscasoBack.message.createMessage.domain;

public interface CreateMessageRepositoryPort {
    void saveMessage(Long roomId, String authorEmail, String content);
}