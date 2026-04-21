package com.daw.forumAscasoBack.room.shared.infrastructure.persistence;

import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class RoomJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_under_moderation", nullable = false)
    private boolean isUnderModeration;

    @Column(nullable = false)
    private boolean isModerated; // NUEVA COLUMNA

    public RoomJpaEntity() {}

    // Actualiza tu constructor
    public RoomJpaEntity(String name, String description, boolean isModerated) {
        this.name = name;
        this.description = description;
        this.isModerated = isModerated;
    }

    // Añade los Getters y Setters
    public boolean isModerated() { return isModerated; }
    public void setModerated(boolean isModerated) { this.isModerated = isModerated; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id") // Una sala puede tener un moderador (o null)
    private UserJpaEntity moderator;

    // Getter y Setter
    public UserJpaEntity getModerator() { return moderator; }
    public void setModerator(UserJpaEntity moderator) { this.moderator = moderator; }
}