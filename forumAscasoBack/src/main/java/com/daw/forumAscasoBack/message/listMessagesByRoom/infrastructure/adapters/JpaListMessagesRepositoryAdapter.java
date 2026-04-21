package com.daw.forumAscasoBack.message.listMessagesByRoom.infrastructure.adapters;

import com.daw.forumAscasoBack.message.listMessagesByRoom.domain.ListMessagesRepositoryPort;
import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaListMessagesRepositoryAdapter implements ListMessagesRepositoryPort {

    private final SpringDataMessageRepository springDataRepository;

    public JpaListMessagesRepositoryAdapter(SpringDataMessageRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public List<Message> findByRoomId(Long roomId) {
        // Buscamos en la BBDD y mapeamos cada entidad al objeto de dominio puro
        return springDataRepository.findByRoom_IdOrderByCreationDateAsc(roomId)
                .stream()
                .map(entity -> new Message(
                        entity.getId(),
                        entity.getContent(),
                        entity.getCreationDate(),
                        entity.getRoom().getId(),
                        entity.getAuthor().getUsername() // Sacamos solo el nombre del autor
                ))
                .collect(Collectors.toList());
    }
}