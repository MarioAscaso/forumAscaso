package com.daw.forumAscasoBack.message.shared.domain.model;

import java.time.LocalDateTime;

public class Message {
    private final Long id;
    private final String content;
    private final LocalDateTime creationDate;
    private final Long roomId;
    private final String authorUsername; // Al frontend le interesa el nombre, no el ID entero del usuario

    public Message(Long id, String content, LocalDateTime creationDate, Long roomId, String authorUsername) {
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
        this.roomId = roomId;
        this.authorUsername = authorUsername;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }
}