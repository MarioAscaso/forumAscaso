package com.daw.forumAscasoBack.room.shared.infrastructure.persistence;

import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.*;

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

    @Column(nullable = false)
    private boolean isModerated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private UserJpaEntity moderator;

    public RoomJpaEntity() {}

    public RoomJpaEntity(String name, String description, boolean isModerated) {
        this.name = name;
        this.description = description;
        this.isModerated = isModerated;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isModerated() { return isModerated; }
    public void setModerated(boolean isModerated) { this.isModerated = isModerated; }
    public UserJpaEntity getModerator() { return moderator; }
    public void setModerator(UserJpaEntity moderator) { this.moderator = moderator; }
}