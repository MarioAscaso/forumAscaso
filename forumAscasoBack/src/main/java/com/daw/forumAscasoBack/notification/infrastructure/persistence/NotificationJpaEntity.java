package com.daw.forumAscasoBack.notification.infrastructure.persistence;

import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class NotificationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user; // El usuario que recibe la mención

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private MessageJpaEntity message; // El mensaje donde le mencionaron

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRead = false;

    public NotificationJpaEntity() {}

    public NotificationJpaEntity(UserJpaEntity user, MessageJpaEntity message, LocalDateTime createdAt) {
        this.user = user;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public UserJpaEntity getUser() { return user; }
    public MessageJpaEntity getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}