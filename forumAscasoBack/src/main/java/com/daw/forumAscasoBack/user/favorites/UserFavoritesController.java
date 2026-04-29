package com.daw.forumAscasoBack.user.favorites;

import com.daw.forumAscasoBack.message.shared.infrastructure.persistence.SpringDataMessageRepository;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/me")
@CrossOrigin(origins = "*")
public class UserFavoritesController {

    private final SpringDataUserRepository userRepository;
    private final SpringDataRoomRepository roomRepository;
    private final SpringDataMessageRepository messageRepository;

    public UserFavoritesController(SpringDataUserRepository userRepository,
                                   SpringDataRoomRepository roomRepository,
                                   SpringDataMessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    // --- 1. LÓGICA DE FAVORITOS ---
    @PostMapping("/favorites/{roomId}")
    @Transactional
    public ResponseEntity<?> toggleFavorite(@PathVariable Long roomId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserJpaEntity user = userRepository.findByEmail(email).orElseThrow();
        RoomJpaEntity room = roomRepository.findById(roomId).orElseThrow();

        boolean isFavorite = user.getFavoriteRooms().contains(room);
        if (isFavorite) {
            user.getFavoriteRooms().remove(room); // Si ya es favorita, la quitamos
        } else {
            user.getFavoriteRooms().add(room); // Si no, la añadimos
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("isFavorite", !isFavorite));
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getMyFavorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserJpaEntity user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(user.getFavoriteRooms());
    }

    // --- 2. LÓGICA DE HISTORIAL ---
    @GetMapping("/history")
    public ResponseEntity<?> getMyHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserJpaEntity user = userRepository.findByEmail(email).orElseThrow();
        // Devuelve los mensajes del usuario ordenados del más reciente al más antiguo
        return ResponseEntity.ok(messageRepository.findByAuthor_IdOrderByCreationDateDesc(user.getId()));
    }
}