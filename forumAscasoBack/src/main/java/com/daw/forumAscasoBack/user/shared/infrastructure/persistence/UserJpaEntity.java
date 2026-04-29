package com.daw.forumAscasoBack.user.shared.infrastructure.persistence;

import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // Campo para la fecha de fin de baneo temporal
    private LocalDateTime banUntil;

    // MEJORA: Cambiado a LAZY para no saturar la base de datos
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_favorite_rooms",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<RoomJpaEntity> favoriteRooms = new HashSet<>();

    public UserJpaEntity() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDateTime getBanUntil() { return banUntil; }
    public void setBanUntil(LocalDateTime banUntil) { this.banUntil = banUntil; }
    public Set<RoomJpaEntity> getFavoriteRooms() { return favoriteRooms; }
    public void setFavoriteRooms(Set<RoomJpaEntity> favoriteRooms) { this.favoriteRooms = favoriteRooms; }
}