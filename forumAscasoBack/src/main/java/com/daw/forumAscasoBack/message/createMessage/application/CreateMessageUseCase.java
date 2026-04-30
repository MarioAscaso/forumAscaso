package com.daw.forumAscasoBack.message.createMessage.application;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import com.daw.forumAscasoBack.notification.infrastructure.persistence.NotificationJpaEntity;
import com.daw.forumAscasoBack.notification.infrastructure.persistence.SpringDataNotificationRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CreateMessageUseCase {

    private final CreateMessageRepositoryPort messageRepository;
    private final SpringDataUserRepository userRepository;
    private final SpringDataNotificationRepository notificationRepository;

    public CreateMessageUseCase(CreateMessageRepositoryPort messageRepository,
                                SpringDataUserRepository userRepository,
                                SpringDataNotificationRepository notificationRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public void execute(Message message) {

        // Probamos con "create" en lugar de "save"
        messageRepository.create(message);

        // Ajustamos los getters asumiendo que tienes objetos relacionados en tu Message
        String content = message.getContent();
        String authorName = message.getAuthor() != null ? message.getAuthor().getUsername() : "usuario";
        Long roomId = message.getRoom() != null ? message.getRoom().getId() : null;

        processMentions(content, authorName, roomId);
    }

    private void processMentions(String messageContent, String authorName, Long roomId) {
        if (messageContent == null) return;

        // Expresión regular: Busca el símbolo @ seguido de caracteres alfanuméricos
        Pattern pattern = Pattern.compile("@(\\w+)");
        Matcher matcher = pattern.matcher(messageContent);

        while (matcher.find()) {
            String mentionedUsername = matcher.group(1);

            // Buscamos si el usuario existe en la BD
            Optional<UserJpaEntity> mentionedUser = userRepository.findByUsername(mentionedUsername);

            if (mentionedUser.isPresent()) {
                String alertText = "El usuario " + authorName + " te ha mencionado en una sala.";

                // Creamos y guardamos la alerta
                NotificationJpaEntity notification = new NotificationJpaEntity(
                        mentionedUser.get(),
                        alertText
                );
                notificationRepository.save(notification);
            }
        }
    }
}