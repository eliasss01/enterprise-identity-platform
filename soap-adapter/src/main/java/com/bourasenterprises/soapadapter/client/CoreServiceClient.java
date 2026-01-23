package com.bourasenterprises.soapadapter.client;

import com.bourasenterprises.soapadapter.client.dto.UserResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CoreServiceClient {

    private final RestTemplate restTemplate;
    private final String coreServiceUrl;

    public CoreServiceClient(RestTemplateBuilder builder,
                             @Value("${app.core-service-url}") String coreServiceUrl) {
        this.restTemplate = builder.build();
        this.coreServiceUrl = coreServiceUrl;
    }


    public UserResponse getUser(Long id) {
        // Recupera il token dall'attuale contesto di sicurezza di Spring
        String token = ((Jwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal())
                .getTokenValue();

        String correlationId = MDC.get("correlationId");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Aggiunge "Authorization: Bearer ..."
        if (correlationId != null) headers.set("X-Correlation-Id", correlationId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                coreServiceUrl + "/api/v1/users/" + id,
                HttpMethod.GET,
                requestEntity,
                UserResponse.class
        ).getBody();
    }

}
