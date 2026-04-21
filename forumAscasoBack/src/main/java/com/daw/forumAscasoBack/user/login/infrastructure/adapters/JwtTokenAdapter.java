package com.daw.forumAscasoBack.user.login.infrastructure.adapters;

import com.daw.forumAscasoBack.user.login.domain.JwtTokenPort;
import com.daw.forumAscasoBack.user.shared.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenAdapter implements JwtTokenPort {

    private final String SECRET = "MiClaveSuperSecretaParaElForoAscaso2024QueNadieDebeSaber";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole()) // <-- AÑADE ESTO
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key)
                .compact();
    }
}