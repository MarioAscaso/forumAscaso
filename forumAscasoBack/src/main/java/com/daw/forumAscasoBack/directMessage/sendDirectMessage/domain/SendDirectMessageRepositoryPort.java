package com.daw.forumAscasoBack.directMessage.sendDirectMessage.domain;

public interface SendDirectMessageRepositoryPort {
    void sendWarning(String senderEmail, Long receiverId, String content);
}