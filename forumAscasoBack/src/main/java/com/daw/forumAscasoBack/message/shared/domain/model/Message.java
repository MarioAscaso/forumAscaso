package com.daw.forumAscasoBack.message.shared.domain.model;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String status;
    private Long authorId; // <-- VITAL para los baneos

    // 🔥 CORRECCIÓN: Se debe llamar exactamente 'username' para que React lo lea
    private String username;

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}