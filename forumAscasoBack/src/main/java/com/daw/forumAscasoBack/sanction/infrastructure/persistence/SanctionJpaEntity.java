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

    // --- AQUÍ ESTÁN LOS GETTERS Y SETTERS QUE FALTABAN ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserJpaEntity getUser() { return user; }
    public void setUser(UserJpaEntity user) { this.user = user; }

    public RoomJpaEntity getRoom() { return room; }
    public void setRoom(RoomJpaEntity room) { this.room = room; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}