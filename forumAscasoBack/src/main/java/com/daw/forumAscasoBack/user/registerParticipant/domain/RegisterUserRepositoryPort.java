package com.daw.forumAscasoBack.user.registerParticipant.domain;

import com.daw.forumAscasoBack.user.shared.domain.model.User;

public interface RegisterUserRepositoryPort {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void save(User user);
}