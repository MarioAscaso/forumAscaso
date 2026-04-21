package com.daw.forumAscasoBack.room.createRoom.domain;

public class CreateRoomCommand {
    private String name;
    private String description;
    private boolean isModerated; // NUEVO CAMPO

    public CreateRoomCommand() {}

    public CreateRoomCommand(String name, String description, boolean isModerated) {
        this.name = name;
        this.description = description;
        this.isModerated = isModerated;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isModerated() { return isModerated; }
    public void setModerated(boolean moderated) { isModerated = moderated; }
}