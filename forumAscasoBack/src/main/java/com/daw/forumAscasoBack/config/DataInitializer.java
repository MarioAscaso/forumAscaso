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
            // COMPROBACIÓN GLOBAL: Solo insertamos si la tabla de usuarios está completamente vacía
            if (repository.count() == 0) {

                // 1. Crear el Superadmin
                repository.save(new UserJpaEntity(
                        "superadmin@test.com",
                        "superadmin",
                        passwordEncoder.encode("superadmin123"),
                        "SUPERADMIN"
                ));

                // 2. Crear el Moderador
                repository.save(new UserJpaEntity(
                        "mod@test.com",
                        "moderador",
                        passwordEncoder.encode("moderador123"),
                        "MODERATOR"
                ));

                // 3. Crear el Participante normal
                repository.save(new UserJpaEntity(
                        "thesamba@test.com",
                        "thesamba",
                        passwordEncoder.encode("thesamba123"),
                        "PARTICIPANT"
                ));

                repository.save(new UserJpaEntity(
                        "remus@test.com",
                        "remus",
                        passwordEncoder.encode("remus123"),
                        "PARTICIPANT"
                ));

                System.out.println("✅ Base de datos vacía detectada: Usuarios de prueba generados e insertados con éxito.");
            } else {
                System.out.println("✅ La base de datos ya contiene usuarios. Omitiendo la inicialización automática.");
            }
        };
    }
}