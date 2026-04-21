package com.daw.forumAscasoBack.room.shared.domain.model;

public class Room {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean isModerated; // NUEVO CAMPO

    public Room(Long id, String name, String description, boolean isModerated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isModerated = isModerated;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isModerated() { return isModerated; } // NUEVO GETTER
}