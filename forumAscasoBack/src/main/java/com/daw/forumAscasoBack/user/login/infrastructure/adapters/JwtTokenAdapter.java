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

    // En un proyecto real, esta clave súper secreta debe estar en tu application.properties
    // Tiene que ser una cadena muy larga (mínimo 256 bits / 32 caracteres)
    private final String SECRET = "MiClaveSuperSecretaParaElForoAscaso2024QueNadieDebeSaber";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Override
    public String generateToken(User user) {
        long expirationTimeInMs = 1000 * 60 * 60 * 24; // El token dura 24 horas

        return Jwts.builder()
                .subject(user.getEmail()) // Identificador principal
                .claim("userId", user.getId()) // Guardamos su ID dentro del token
                .claim("role", user.getRole().name()) // Guardamos su Rol (vital para el foro)
                .issuedAt(new Date()) // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + expirationTimeInMs)) // Cuándo caduca
                .signWith(key) // Firmamos con nuestra clave secreta
                .compact(); // Construimos el String final
    }
}