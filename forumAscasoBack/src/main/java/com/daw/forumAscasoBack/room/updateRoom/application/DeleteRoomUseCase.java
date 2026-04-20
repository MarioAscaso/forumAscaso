package com.daw.forumAscasoBack.room.updateRoom.application;

public class DeleteRoomUseCase {

    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Sala no encontrada");
        }
        repository.deleteById(id);
    }

}
