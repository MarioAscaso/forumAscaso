package com.daw.forumAscasoBack.user.registerParticipant.domain;

public record RegisterParticipantCommand(
        String username,
        String email,
        String password
) {}