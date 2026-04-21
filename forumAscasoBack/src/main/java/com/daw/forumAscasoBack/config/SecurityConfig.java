package com.daw.forumAscasoBack.config;

import com.daw.forumAscasoBack.config.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Inyectamos el filtro JWT que creamos para procesar los tokens
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Deshabilitamos CSRF ya que las APIs REST con JWT son "stateless"
                .csrf(AbstractHttpConfigurer::disable)

                // Configuramos la gestión de sesiones como sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Permitimos el acceso público al registro y al login
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()

                        // TODOS los usuarios logueados pueden VER las salas (GET)
                        .requestMatchers(HttpMethod.GET, "/api/rooms").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/messages/room/**").authenticated()

                        // Solo el SUPERADMIN puede CREAR (POST), MODIFICAR (PUT) o BORRAR (DELETE) salas
                        .requestMatchers("/api/rooms", "/api/rooms/**").hasRole("SUPERADMIN")

                        // Cualquier otra petición requiere autenticación previa
                        .anyRequest().authenticated()
                )
                // Añadimos nuestro filtro personalizado antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Definimos el bean para encriptar contraseñas con BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Asegúrate que sea el puerto de tu front
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Muy importante para JWT
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // CAMBIA ESTA LÍNEA:
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}