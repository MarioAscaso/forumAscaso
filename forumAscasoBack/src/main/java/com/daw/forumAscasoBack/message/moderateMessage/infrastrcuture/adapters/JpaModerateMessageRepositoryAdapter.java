package com.daw.forumAscasoBack.message.moderateMessage.infrastructure.adapters;

import com.daw.forumAscasoBack.message.moderateMessage.domain.ModerateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaModerateMessageRepositoryAdapter implements ModerateMessageRepositoryPort {

    private final SpringDataMessageRepository messageRepository;

    public JpaModerateMessageRepositoryAdapter(SpringDataMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void updateMessageStatus(Long messageId, String newStatus) {
        // Buscamos el mensaje
        MessageJpaEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        // Le cambiamos el estado y lo guardamos
        message.setStatus(newStatus);
        messageRepository.save(message);
    }
}