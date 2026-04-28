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
                UserJpaEntity superadmin = new UserJpaEntity();
                superadmin.setEmail("superadmin@test.com");
                superadmin.setUsername("superadmin");
                superadmin.setPassword(passwordEncoder.encode("superadmin123"));
                superadmin.setRole("SUPERADMIN");
                repository.save(superadmin);

                // 2. Crear el Moderador
                UserJpaEntity mod = new UserJpaEntity();
                mod.setEmail("moderador@test.com");
                mod.setUsername("moderador");
                mod.setPassword(passwordEncoder.encode("moderador123"));
                mod.setRole("MODERATOR");
                repository.save(mod);

                // 3. Crear el Participante normal (thesamba)
                UserJpaEntity thesamba = new UserJpaEntity();
                thesamba.setEmail("thesamba@test.com");
                thesamba.setUsername("thesamba");
                thesamba.setPassword(passwordEncoder.encode("thesamba123"));
                thesamba.setRole("PARTICIPANT");
                repository.save(thesamba);

                // 4. Crear otro Participante (remus)
                UserJpaEntity remus = new UserJpaEntity();
                remus.setEmail("remus@test.com");
                remus.setUsername("remus");
                remus.setPassword(passwordEncoder.encode("remus123"));
                remus.setRole("PARTICIPANT");
                repository.save(remus);

                System.out.println("✅ Base de datos vacía detectada: Usuarios de prueba generados e insertados con éxito.");
            } else {
                System.out.println("✅ La base de datos ya contiene usuarios. Omitiendo la inicialización automática.");
            }
        };
    }
}