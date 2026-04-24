package com.daw.forumAscasoBack.user.shared.domain.model;

import java.time.LocalDateTime;

public class User {
    public enum Role {
        PARTICIPANT, MODERATOR, SUPERADMIN, BANNED, PERNABANNED
    }

    private Long id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private LocalDateTime banUntil;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDateTime getBanUntil() { return banUntil; }
    public void setBanUntil(LocalDateTime banUntil) { this.banUntil = banUntil; }
}