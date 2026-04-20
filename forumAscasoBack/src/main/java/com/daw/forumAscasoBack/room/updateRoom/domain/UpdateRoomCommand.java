package com.daw.forumAscasoBack.room.updateRoom.domain;

public record UpdateRoomCommand(
        Long id,
        String name,
        String description,
        boolean isUnderModeration
) {}