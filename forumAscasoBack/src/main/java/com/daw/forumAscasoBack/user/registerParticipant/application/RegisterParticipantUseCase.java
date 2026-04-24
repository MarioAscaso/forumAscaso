package com.daw.forumAscasoBack.user.registerParticipant.application;

import com.daw.forumAscasoBack.user.registerParticipant.domain.RegisterParticipantCommand;
import com.daw.forumAscasoBack.user.registerParticipant.domain.RegisterUserRepositoryPort;
import com.daw.forumAscasoBack.user.registerParticipant.domain.PasswordEncoderPort;
import com.daw.forumAscasoBack.user.shared.domain.model.User;

public class RegisterParticipantUseCase {

    private final RegisterUserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    // Inyección de dependencias por constructor
    public RegisterParticipantUseCase(RegisterUserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(RegisterParticipantCommand command) {
        // 1. Validar reglas de negocio
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // 2. Crear la entidad de dominio (con rol PARTICIPANT por defecto)
        User newUser = new User();
        newUser.setUsername(command.username());
        newUser.setEmail(command.email());

        // CORRECCIÓN 1: Usamos setPassword() en lugar de setPasswordHash()
        newUser.setPassword(passwordEncoder.encode(command.password()));

        // CORRECCIÓN 2: Usamos nuestro Enum para el rol
        newUser.setRole(User.Role.PARTICIPANT);

        // CORRECCIÓN 3: Eliminamos newUser.setStatus(...) porque ahora usamos el rol BANNED para los bloqueos

        // 3. Guardar en la base de datos
        userRepository.save(newUser);
    }
}