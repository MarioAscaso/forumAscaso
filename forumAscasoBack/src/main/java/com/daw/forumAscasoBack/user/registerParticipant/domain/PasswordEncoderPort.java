package com.daw.forumAscasoBack.user.registerParticipant.domain;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}