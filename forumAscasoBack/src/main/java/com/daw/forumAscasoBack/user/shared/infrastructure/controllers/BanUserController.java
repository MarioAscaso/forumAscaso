package com.daw.forumAscasoBack.user.shared.infrastructure.controllers;

import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class BanUserController {
    private final SpringDataUserRepository userRepository;

    public BanUserController(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PatchMapping("/{userId}/ban")
    @Transactional
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setRole("PERNABANNED"); // Nueva etiqueta para expulsión definitiva
            user.setBanUntil(null);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Usuario expulsado permanentemente"));
        }).orElse(ResponseEntity.notFound().build());
    }
}