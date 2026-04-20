package com.daw.forumAscasoBack.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // IMPORTANTE: Esta debe ser EXACTAMENTE la misma clave que pusiste en JwtTokenAdapter
    private final String SECRET = "MiClaveSuperSecretaParaElForoAscaso2024QueNadieDebeSaber";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraemos la cabecera "Authorization" de la petición HTTP
        String authHeader = request.getHeader("Authorization");

        // 2. Comprobamos si tiene un Token válido (debe empezar por "Bearer ")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Si no hay token, seguimos (Spring Security lo bloqueará si la ruta es privada)
        }

        // 3. Quitamos la palabra "Bearer " para quedarnos solo con el churro del Token
        String token = authHeader.substring(7);

        try {
            // 4. Desencriptamos el Token usando nuestra clave secreta
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 5. Extraemos los datos que guardamos en el login
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            // 6. Le decimos a Spring Security que este usuario está autenticado y le damos su ROL
            // Spring requiere que los roles empiecen por "ROLE_" para usar sus anotaciones
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email, null, Collections.singletonList(authority));

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // Si el token es falso, ha caducado o está modificado, limpiamos el contexto
            SecurityContextHolder.clearContext();
        }

        // 7. Continuamos con la petición hacia el Controlador
        filterChain.doFilter(request, response);
    }
}