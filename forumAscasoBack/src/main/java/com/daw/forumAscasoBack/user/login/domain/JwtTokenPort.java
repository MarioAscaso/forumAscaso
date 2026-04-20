package com.daw.forumAscasoBack.user.login.domain;

import com.daw.forumAscasoBack.user.shared.domain.model.User;

public interface JwtTokenPort {
    String generateToken(User user);
}