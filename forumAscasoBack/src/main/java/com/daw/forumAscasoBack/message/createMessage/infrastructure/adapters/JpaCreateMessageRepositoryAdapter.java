package com.daw.forumAscasoBack.message.createMessage.infrastructure.adapters;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JpaCreateMessageRepositoryAdapter implements CreateMessageRepositoryPort {

    private final SpringDataMessageRepository messageRepository;
    private final SpringDataRoomRepository roomRepository;
    private final SpringDataUserRepository userRepository;

    public JpaCreateMessageRepositoryAdapter(SpringDataMessageRepository messageRepository,
                                             SpringDataRoomRepository roomRepository,
                                             SpringDataUserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveMessage(Long roomId, String authorEmail, String content) {
        UserJpaEntity author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RoomJpaEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        // AÑADIMOS "APPROVED" AL CONSTRUCTOR TEMPORALMENTE
        MessageJpaEntity newMessage = new MessageJpaEntity(content, LocalDateTime.now(), "APPROVED", room, author);
        messageRepository.save(newMessage);
    }
}