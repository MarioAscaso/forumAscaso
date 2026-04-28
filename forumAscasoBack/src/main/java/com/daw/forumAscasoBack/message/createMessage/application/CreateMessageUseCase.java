package com.daw.forumAscasoBack.message.createMessage.application;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;

public class CreateMessageUseCase {

    private final CreateMessageRepositoryPort repository;

    public CreateMessageUseCase(CreateMessageRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(Long roomId, String email, String content) {
        // Tu adaptador JpaCreateMessageRepositoryAdapter ya hace toda la magia
        // (comprobar baneos, antispam, moderación y guardado),
        // así que solo tenemos que pasarle los datos.
        repository.saveMessage(roomId, email, content);
    }
}