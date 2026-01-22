package com.bourasenterprises.identity.core.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    @Value("${KEYCLOAK_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${KEYCLOAK_SERVER_URL:http://keycloak:8080}")
    private String serverUrl;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("enterprise-platform")
                .grantType("client_credentials")
                .clientId("core-service")
                .clientSecret(clientSecret)
                .build();
    }
}