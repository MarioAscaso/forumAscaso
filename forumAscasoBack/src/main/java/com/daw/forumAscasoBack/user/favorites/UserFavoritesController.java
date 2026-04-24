package com.daw.forumAscasoBack.user.favorites;

import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class UserFavoritesController {

    private final SpringDataUserRepository userRepository;
    private final SpringDataRoomRepository roomRepository;

    public UserFavoritesController(SpringDataUserRepository userRepository, SpringDataRoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // Alternar (Toggle) de Favorito
    @PostMapping("/{email}/{roomId}")
    public ResponseEntity<?> toggleFavorite(@PathVariable String email, @PathVariable Long roomId) {
        UserJpaEntity user = userRepository.findByEmail(email).orElseThrow();
        RoomJpaEntity room = roomRepository.findById(roomId).orElseThrow();

        if (user.getFavoriteRooms().contains(room)) {
            user.getFavoriteRooms().remove(room); // Si ya es favorito, lo quita
        } else {
            user.getFavoriteRooms().add(room); // Si no, lo añade
        }
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // Obtener IDs de salas favoritas del usuario
    @GetMapping("/{email}")
    public ResponseEntity<Set<Long>> getFavorites(@PathVariable String email) {
        UserJpaEntity user = userRepository.findByEmail(email).orElseThrow();
        Set<Long> favoriteIds = user.getFavoriteRooms().stream()
                .map(RoomJpaEntity::getId)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(favoriteIds);
    }
}