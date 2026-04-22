package com.daw.forumAscasoBack.user.login.application;

import com.daw.forumAscasoBack.user.login.domain.JwtTokenPort;
import com.daw.forumAscasoBack.user.login.domain.LoginCommand;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository;
import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginUseCase {

    private final SpringDataUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public LoginUseCase(SpringDataUserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    public Map<String, String> execute(LoginCommand command) {
        // 1. Buscamos al usuario por email
        UserJpaEntity userEntity = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // 2. CORRECCIÓN: Usamos getPassword() en lugar de getPasswordHash()
        if (!passwordEncoder.matches(command.password(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Convertimos la entidad JPA al modelo de Dominio
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setUsername(userEntity.getUsername());

        // CORRECCIÓN: getRole() ahora es un String directo
        user.setRole(User.Role.valueOf(userEntity.getRole()));

        // 4. Generamos el Token
        String token = jwtTokenPort.generateToken(user);

        // 5. Preparamos la respuesta que React necesita
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", userEntity.getRole());
        response.put("username", userEntity.getUsername());

        return response;
    }
}