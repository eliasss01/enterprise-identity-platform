package com.bourasenterprises.identity.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        // STEP 1: Rendiamo la creazione utente PUBBLICA per il funnel
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter())
                ));

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthConverter(){
        return new JwtAuthenticationConverter();
    }

    @Value("${app.security.cors.allowed-origins}")
    List<String> allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Possono accedere solo gli url nelle config.yml
        configuration.setAllowedOrigins(allowedOrigins);

        // Metodi permessi
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permessi
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        // In produzione è bene non permettere l'invio di cookie se non strettamente necessario
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // applicazione della config a tutte le rotte dell' ms
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
