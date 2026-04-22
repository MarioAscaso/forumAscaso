package com.daw.forumAscasoBack.message.createMessage.infrastructure.adapters;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.MessageJpaEntity;
import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
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
    private final SpringDataSanctionRepository sanctionRepository;

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

        // 1. REQUISITO: Comprobación de Sanciones (Baneos)
        List<SanctionJpaEntity> sanctions = sanctionRepository.findByUser_Id(author.getId());
        boolean isBanned = sanctions.stream().anyMatch(s ->
                s.getType().contains("BAN") &&
                (s.getExpiryDate() == null || s.getExpiryDate().isAfter(LocalDateTime.now()))
        );

        if (isBanned) {
            // Este texto exacto será capturado por React para mostrar el Alert personalizado
            throw new RuntimeException("Tu cuenta ha sido bloqueada debido a incumplimientos en las normas de la comunidad.");
        }

        // 2. REQUISITO: Límite Anti-Spam (Máximo 10 mensajes última semana)
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        long messageCount = messageRepository.countByAuthor_IdAndCreationDateAfter(author.getId(), oneWeekAgo);

        if (messageCount >= 10) {
            throw new RuntimeException("Has alcanzado el límite de 10 mensajes semanales permitido por el centro.");
        }

        RoomJpaEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        // 3. MEJORA: Los mods y admins NO pasan por moderación (Se auto-aprueban)
        String initialStatus = "APPROVED";
        boolean isStaff = "MODERATOR".equals(author.getRole()) || "SUPERADMIN".equals(author.getRole());

        if (room.isModerated() && !isStaff) {
            initialStatus = "PENDING";
        }

        // 4. Guardado final en base de datos
        MessageJpaEntity newMessage = new MessageJpaEntity(
                content,
                LocalDateTime.now(),
                initialStatus,
                room,
                author
        );

        messageRepository.save(newMessage);
    }
}