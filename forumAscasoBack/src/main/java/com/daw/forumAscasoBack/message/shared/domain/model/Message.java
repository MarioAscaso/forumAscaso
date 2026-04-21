package com.daw.forumAscasoBack.message.shared.domain.model;

import java.time.LocalDateTime;

public class Message {
    private final Long id;
    private final String content;
    private final LocalDateTime creationDate;
    private final Long roomId;
    private final String authorUsername;
    private final String status; // NUEVO CAMPO

    public Message(Long id, String content, LocalDateTime creationDate, Long roomId, String authorUsername, String status) {
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
        this.roomId = roomId;
        this.authorUsername = authorUsername;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Long getRoomId() { return roomId; }
    public String getAuthorUsername() { return authorUsername; }
    public String getStatus() { return status; } // NUEVO GETTER
}