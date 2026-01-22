package com.bourasenterprises.identity.core.application;

import com.bourasenterprises.identity.core.infrastructure.kafka.events.SubscriptionActivatedEvent;
import com.bourasenterprises.identity.core.infrastructure.kafka.messaging.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.bourasenterprises.identity.core.domain.UserEntity;
import com.bourasenterprises.identity.core.mapper.UserMapper;
import com.bourasenterprises.identity.core.service.UserService;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService service;
    private final UserMapper mapper;
    private final KafkaProducerService kafkaProducer;

    public UserResponse createUser(CreateUserRequest request){
        log.info("Inizio funnel registrazione per email: {}", request.getEmail());

        UserEntity entity = mapper.toEntity(request);
        UserEntity savedEntity = service.createUser(entity);

        // INVIO EVENTO KAFKA
        // Usiamo l'UUID di Keycloak come userId per l'evento
        try {
            SubscriptionActivatedEvent event = new SubscriptionActivatedEvent(
                    savedEntity.getKeycloakId().toString(),
                    "SUB-BASIC-" + savedEntity.getKeycloakId(),
                    java.time.Instant.now()
            );

            kafkaProducer.publishSubscriptionActivated(event);
            log.info("Evento Kafka inviato per l'utente: {}", savedEntity.getKeycloakId());
        } catch (Exception e) {
            // Logghiamo l'errore ma non blocchiamo la risposta all'utente
            // TODO un "Outbox Pattern"
            log.error("Errore invio evento Kafka per utente {}: {}", savedEntity.getKeycloakId(), e.getMessage());
        }

        UserResponse response = mapper.toResponse(savedEntity);
        log.info("Operatore creato con successo. ID: {}", response.getId());
        return response;
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('OPERATOR')")
    public UserResponse getUser(Long id){
        return mapper.toResponse(service.getUser(id));
    }
}