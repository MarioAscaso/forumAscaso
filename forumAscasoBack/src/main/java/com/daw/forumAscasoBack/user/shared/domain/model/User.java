package com.daw.forumAscasoBack.user.shared.domain.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;
    private Status status;

    public enum Role { SUPERADMIN, MODERATOR, PARTICIPANT }
    public enum Status { ACTIVE, DEACTIVATED }
}