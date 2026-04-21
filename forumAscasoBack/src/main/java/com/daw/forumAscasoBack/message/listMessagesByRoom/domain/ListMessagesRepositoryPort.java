package com.daw.forumAscasoBack.message.listMessagesByRoom.domain;

import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import java.util.List;

public interface ListMessagesRepositoryPort {
    List<Message> findByRoomId(Long roomId);
}