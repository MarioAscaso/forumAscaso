package com.daw.forumAscasoBack.directMessage.sendDirectMessage.infrastructure.adapters;

import com.daw.forumAscasoBack.directMessage.shared.infrastructure.persistence.DirectMessageJpaEntity;
import com.daw.forumAscasoBack.directMessage.shared.infrastructure.persistence.SpringDataDirectMessageRepository;
import com.daw.forumAscasoBack.directMessage.sendDirectMessage.domain.SendDirectMessageRepositoryPort;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class JpaSendDirectMessageRepositoryAdapter implements SendDirectMessageRepositoryPort {

    private final SpringDataDirectMessageRepository directMessageRepository;
    private final SpringDataUserRepository userRepository;

    public JpaSendDirectMessageRepositoryAdapter(
            SpringDataDirectMessageRepository directMessageRepository,
            SpringDataUserRepository userRepository) {
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void sendWarning(String senderEmail, Long receiverId, String content) {
        UserJpaEntity sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Moderador no encontrado"));

        UserJpaEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Usuario destino no encontrado"));

        DirectMessageJpaEntity message = new DirectMessageJpaEntity(sender, receiver, content);
        directMessageRepository.save(message);
    }
}