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
        UserJpaEntity userEntity = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncoder.matches(command.password(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setUsername(userEntity.getUsername());

        // CONVERTIMOS DE STRING (BD) A ENUM (DOMINIO)
        try {
            user.setRole(User.Role.valueOf(userEntity.getRole().trim().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            user.setRole(User.Role.PARTICIPANT); // Valor por defecto si hay error en BD
        }

        String token = jwtTokenPort.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        // Volvemos a pasarlo a String para enviarlo a React
        response.put("role", user.getRole().name());
        response.put("username", user.getUsername());

        return response;
    }
}