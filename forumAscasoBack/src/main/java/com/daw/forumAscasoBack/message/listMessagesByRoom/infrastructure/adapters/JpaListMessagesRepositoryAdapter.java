package com.daw.forumAscasoBack.message.listMessagesByRoom.infrastructure.adapters;

import com.daw.forumAscasoBack.message.listMessagesByRoom.domain.ListMessagesRepositoryPort;
import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JpaListMessagesRepositoryAdapter implements ListMessagesRepositoryPort {

    private final SpringDataMessageRepository jpaRepository;

    public JpaListMessagesRepositoryAdapter(SpringDataMessageRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Message> findByRoomId(Long roomId) {
        // Usamos el nombre exacto de tu repositorio
        List<MessageJpaEntity> entities = jpaRepository.findByRoom_IdOrderByCreationDateAsc(roomId);
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Message> findPendingByRoomId(Long roomId) {
        // Usamos el nombre exacto de tu repositorio
        List<MessageJpaEntity> entities = jpaRepository.findByRoom_IdAndStatusOrderByCreationDateAsc(roomId, "PENDING");
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Message toDomain(MessageJpaEntity entity) {
        Message message = new Message();
        message.setId(entity.getId());
        message.setContent(entity.getContent());

        // AQUÍ ESTABA EL ERROR: Llamamos a tu método real (getCreationDate)
        message.setCreatedAt(entity.getCreationDate());

        message.setStatus(entity.getStatus());
        message.setAuthorId(entity.getAuthor().getId());
        message.setAuthorUsername(entity.getAuthor().getUsername());

        return message;
    }
}