package com.daw.forumAscasoBack.message.createMessage.infrastructure.adapters;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import com.daw.forumAscasoBack.notification.infrastructure.persistence.NotificationJpaEntity;
import com.daw.forumAscasoBack.notification.infrastructure.persistence.SpringDataNotificationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JpaCreateMessageRepositoryAdapter implements CreateMessageRepositoryPort {

    private final SpringDataMessageRepository messageRepository;
    private final SpringDataRoomRepository roomRepository;
    private final SpringDataUserRepository userRepository;
    private final SpringDataNotificationRepository notificationRepository;

    public JpaCreateMessageRepositoryAdapter(
            SpringDataMessageRepository messageRepository,
            SpringDataRoomRepository roomRepository,
            SpringDataUserRepository userRepository,
            SpringDataNotificationRepository notificationRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void saveMessage(Long roomId, String email, String content) {
        // 1. Obtener autor y sala
        UserJpaEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        RoomJpaEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        // 2. LÓGICA DE MODERACIÓN PREVIA
        // Verificamos si la sala requiere moderación
        String status = room.isModerated() ? "PENDING" : "APPROVED";

        // 3. Crear el mensaje con el estado calculado
        MessageJpaEntity message = new MessageJpaEntity(
                content,
                LocalDateTime.now(),
                status,
                room,
                author
        );

        MessageJpaEntity savedMessage = messageRepository.save(message);

        // 4. LÓGICA DE MENCIONES (@username)
        processMentions(content, savedMessage);
    }

    private void processMentions(String content, MessageJpaEntity message) {
        // Expresión regular para encontrar palabras que empiecen por @ (ej: @thesamba)
        Pattern pattern = Pattern.compile("@(\\w+)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String username = matcher.group(1); // Obtenemos el nombre sin el arroba

            Optional<UserJpaEntity> mentionedUserOpt = userRepository.findByUsername(username);

            if (mentionedUserOpt.isPresent()) {
                UserJpaEntity mentionedUser = mentionedUserOpt.get();

                // Un usuario no debería notificarse a sí mismo
                if (!mentionedUser.getId().equals(message.getAuthor().getId())) {
                    NotificationJpaEntity notification = new NotificationJpaEntity(
                            mentionedUser,
                            message,
                            LocalDateTime.now()
                    );
                    notificationRepository.save(notification);
                }
            }
        }
    }
}