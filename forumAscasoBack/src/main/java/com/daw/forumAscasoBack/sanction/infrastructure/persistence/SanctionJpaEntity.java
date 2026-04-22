package com.daw.forumAscasoBack.sanction.infrastructure.persistence;

import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sanctions")
public class SanctionJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne
    @JoinColumn(name = "room_id") // Nullable si el baneo es global
    private RoomJpaEntity room;

    @Column(nullable = false)
    private String type; // "WARNING", "TEMPORARY_BAN", "PERMANENT_BAN"

    private String reason;
    private LocalDateTime expiryDate; // null para permanentes
    private LocalDateTime createdAt;

    public SanctionJpaEntity() {}

    public SanctionJpaEntity(UserJpaEntity user, RoomJpaEntity room, String type, String reason, LocalDateTime expiryDate) {
        this.user = user;
        this.room = room;
        this.type = type;
        this.reason = reason;
        this.expiryDate = expiryDate;
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters...
}