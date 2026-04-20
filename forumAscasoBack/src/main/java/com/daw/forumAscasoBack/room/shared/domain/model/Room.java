package com.daw.forumAscasoBack.room.shared.domain.model;

import lombok.Data;

@Data
public class Room {
    private Long id;
    private String name;
    private String description;
    private boolean isUnderModeration; // Requisito: "una sala puede, o no, estar bajo moderación"
}