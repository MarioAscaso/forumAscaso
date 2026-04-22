package com.daw.forumAscasoBack.message.createMessage.infrastructure.adapters;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;

// IMPORTAMOS NUESTRAS SANCIONES
import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SpringDataSanctionRepository;
import com.daw.forumAscasoBack.sanction.infrastructure.persistence.SanctionJpaEntity;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class JpaCreateMessageRepositoryAdapter implements CreateMessageRepositoryPort {

    private final SpringDataMessageRepository messageRepository;
    private final SpringDataUserRepository userRepository;
    private final SpringDataRoomRepository roomRepository;
    private final SpringDataSanctionRepository sanctionRepository; // NUEVO REPOSITORIO

    // Lo añadimos al constructor
    public JpaCreateMessageRepositoryAdapter(SpringDataMessageRepository messageRepository,
                                             SpringDataUserRepository userRepository,
                                             SpringDataRoomRepository roomRepository,
                                             SpringDataSanctionRepository sanctionRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.sanctionRepository = sanctionRepository;
    }

    @Override
    public void saveMessage(Long roomId, String authorEmail, String content) {
        UserJpaEntity author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // --- MAGIA DE LA MODERACIÓN: COMPROBAR BANEOS ---
        List<SanctionJpaEntity> sanctions = sanctionRepository.findByUser_Id(author.getId());

        // Comprobamos si hay alguna sanción tipo BAN que sea PERMANENTE (fecha nula) o TEMPORAL (fecha en el futuro)
        boolean isBanned = sanctions.stream().anyMatch(s ->
                s.getType().contains("BAN") &&
                (s.getExpiryDate() == null || s.getExpiryDate().isAfter(LocalDateTime.now()))
        );

        if (isBanned) {
            // Si está baneado, detenemos el proceso con un error
            throw new RuntimeException("No puedes publicar mensajes. Tienes un bloqueo activo por mala conducta.");
        }
        // ------------------------------------------------

        RoomJpaEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        String initialStatus = room.isModerated() ? "PENDING" : "APPROVED";

        MessageJpaEntity newMessage = new MessageJpaEntity(content, LocalDateTime.now(), initialStatus, room, author);
        messageRepository.save(newMessage);
    }
}