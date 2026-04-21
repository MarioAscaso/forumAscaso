package com.daw.forumAscasoBack.message.moderateMessage.domain;

public interface ModerateMessageRepositoryPort {
    void updateMessageStatus(Long messageId, String newStatus);
}