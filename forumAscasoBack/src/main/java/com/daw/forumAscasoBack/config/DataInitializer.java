package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.RoomJpaEntity;
import com.daw.forumAscasoBack.room.shared.infrastructure.persistence.SpringDataRoomRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            SpringDataUserRepository userRepository,
            SpringDataRoomRepository roomRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // 1. INICIALIZACIÓN DE USUARIOS
            if (userRepository.count() == 0) {
                UserJpaEntity superadmin = new UserJpaEntity();
                superadmin.setEmail("superadmin@test.com");
                superadmin.setUsername("superadmin");
                superadmin.setPassword(passwordEncoder.encode("superadmin123"));
                superadmin.setRole("SUPERADMIN");
                userRepository.save(superadmin);

                UserJpaEntity mod = new UserJpaEntity();
                mod.setEmail("moderador@test.com");
                mod.setUsername("moderador");
                mod.setPassword(passwordEncoder.encode("moderador123"));
                mod.setRole("MODERATOR");
                userRepository.save(mod);

                UserJpaEntity thesamba = new UserJpaEntity();
                thesamba.setEmail("thesamba@test.com");
                thesamba.setUsername("thesamba");
                thesamba.setPassword(passwordEncoder.encode("thesamba123"));
                thesamba.setRole("PARTICIPANT");
                userRepository.save(thesamba);

                UserJpaEntity remus = new UserJpaEntity();
                remus.setEmail("remus@test.com");
                remus.setUsername("remus");
                remus.setPassword(passwordEncoder.encode("remus123"));
                remus.setRole("PARTICIPANT");
                userRepository.save(remus);

                System.out.println("✅ Usuarios de prueba generados e insertados con éxito.");
            }

            // 2. INICIALIZACIÓN DE SALAS
            if (roomRepository.count() == 0) {
                // Sala 1: No moderada
                RoomJpaEntity general = new RoomJpaEntity("Sala General", "Sala para charlar de todo", false);
                general.setUnderModeration(false); // Para cubrir tu otra columna

                // Sala 2: Moderada (Requiere aprobación de mensajes)
                RoomJpaEntity dudas = new RoomJpaEntity("Dudas y Consultas", "Toda pregunta debe ser aprobada", true);
                dudas.setUnderModeration(true);

                // Sala 3: No moderada
                RoomJpaEntity offTopic = new RoomJpaEntity("Off-Topic", "Juegos, música y más", false);
                offTopic.setUnderModeration(false);

                roomRepository.saveAll(List.of(general, dudas, offTopic));
                System.out.println("✅ Salas de prueba generadas e insertadas con éxito.");
            }
        };
    }
}