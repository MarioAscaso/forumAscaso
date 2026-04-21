package com.daw.forumAscasoBack.message.shared.infrastructure.persistence;

import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    // Relación: Muchos mensajes pertenecen a UNA sala
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomJpaEntity room;

    // Relación: Muchos mensajes son escritos por UN usuario (autor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserJpaEntity author;

    // Constructores
    public MessageJpaEntity() {
    }

    public MessageJpaEntity(String content, LocalDateTime creationDate, RoomJpaEntity room, UserJpaEntity author) {
        this.content = content;
        this.creationDate = creationDate;
        this.room = room;
        this.author = author;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public RoomJpaEntity getRoom() {
        return room;
    }

    public void setRoom(RoomJpaEntity room) {
        this.room = room;
    }

    public UserJpaEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserJpaEntity author) {
        this.author = author;
    }
}