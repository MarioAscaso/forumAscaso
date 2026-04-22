package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(SpringDataUserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Comprobamos si existe el Superadmin antes de crearlo
            if (!repository.existsByEmail("admin@test.com")) {
                repository.save(new UserJpaEntity(
                        "admin@test.com",
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "SUPERADMIN"
                ));
            }

            // Comprobamos si existe el Moderador
            if (!repository.existsByEmail("mod@test.com")) {
                repository.save(new UserJpaEntity(
                        "mod@test.com",
                        "moderador",
                        passwordEncoder.encode("mod123"),
                        "MODERATOR"
                ));
            }

            // Comprobamos si existe el Participante normal
            if (!repository.existsByEmail("user@test.com")) {
                repository.save(new UserJpaEntity(
                        "user@test.com",
                        "juan_participante",
                        passwordEncoder.encode("user123"),
                        "PARTICIPANT"
                ));
            }

            System.out.println("✅ Usuarios de prueba verificados y listos en la BD.");
        };
    }
}