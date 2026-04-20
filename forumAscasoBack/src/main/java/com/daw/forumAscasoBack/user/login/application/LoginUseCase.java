package com.daw.forumAscasoBack.user.login.application;

import com.daw.forumAscasoBack.user.login.domain.JwtTokenPort;
import com.daw.forumAscasoBack.user.login.domain.LoginCommand;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUseCase {

    // Nota: Por simplicidad en este caso de uso, usamos directamente el repositorio y el encoder de Spring,
    // aunque en un Hexagonal puro 100% haríamos puertos intermedios para buscar por email y comparar contraseñas.
    private final SpringDataUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public LoginUseCase(SpringDataUserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    public String execute(LoginCommand command) {
        // 1. Buscamos al usuario por email (Necesitamos añadir este método a SpringDataUserRepository)
        UserJpaEntity userEntity = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // 2. Comparamos la contraseña en texto plano con el Hash de la BD
        if (!passwordEncoder.matches(command.password(), userEntity.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Convertimos la entidad JPA al modelo de Dominio puro
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setUsername(userEntity.getUsername());
        user.setRole(User.Role.valueOf(userEntity.getRole().name()));

        // 4. Generamos y devolvemos el Token
        return jwtTokenPort.generateToken(user);
    }
}