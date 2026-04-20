package com.daw.forumAscasoBack.room.createRoom.domain;

public record CreateRoomCommand(
        String name,
        String description,
        boolean isUnderModeration
) {}