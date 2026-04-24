package com.daw.forumAscasoBack.message.createMessage.application;

import com.daw.forumAscasoBack.message.createMessage.domain.CreateMessageRepositoryPort;
import com.daw.forumAscasoBack.message.shared.domain.model.Message;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import java.time.LocalDateTime;

public class CreateMessageUseCase {
    private final CreateMessageRepositoryPort repository;
    private final SpringDataUserRepository userRepository;
    private final SpringDataRoomRepository roomRepository;

    public CreateMessageUseCase(CreateMessageRepositoryPort repository, SpringDataUserRepository userRepository, SpringDataRoomRepository roomRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public void execute(Long roomId, String email, String content) {
        UserJpaEntity sender = userRepository.findByEmail(email).orElseThrow();
        RoomJpaEntity room = roomRepository.findById(roomId).orElseThrow();

        Message newMessage = new Message();
        newMessage.setContent(content);
        newMessage.setCreatedAt(LocalDateTime.now());

        // Auto-validación para MODERATOR y SUPERADMIN
        String role = sender.getRole().toUpperCase();
        boolean isAutoValid = role.equals("SUPERADMIN") || role.equals("MODERATOR");

        // 🔥 CORRECCIÓN AQUÍ: Llamamos a room.isModerated()
        if (isAutoValid) {
            // Si es SuperAdmin o Moderador, se aprueba automáticamente
            newMessage.setStatus("APPROVED");
        } else {
            // Si es Participante, dependerá de si la sala está sujeta a moderación
            newMessage.setStatus(room.isModerated() ? "PENDING" : "APPROVED");
        }

        repository.save(newMessage, roomId, sender.getId());
    }
}