package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.user.registerParticipant.application.RegisterParticipantUseCase;
import com.daw.forumAscasoBack.user.registerParticipant.domain.PasswordEncoderPort;
import com.daw.forumAscasoBack.user.registerParticipant.domain.RegisterUserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserModuleConfig {

    @Bean
    public RegisterParticipantUseCase registerParticipantUseCase(
            RegisterUserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder) {

        return new RegisterParticipantUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public com.daw.forumAscasoBack.user.login.application.LoginUseCase loginUseCase(
            com.daw.forumAscasoBack.user.shared.infrastructure.persistence.SpringDataUserRepository userRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
            com.daw.forumAscasoBack.user.login.domain.JwtTokenPort jwtTokenPort) {

        return new com.daw.forumAscasoBack.user.login.application.LoginUseCase(userRepository, passwordEncoder, jwtTokenPort);
    }
}