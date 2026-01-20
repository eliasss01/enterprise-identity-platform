package com.bourasenterprises.identity.core.application;

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

    public UserResponse createUser(CreateUserRequest request){
        log.info("Inizio funnel registrazione per email: {}", request.getEmail());
        UserEntity entity = mapper.toEntity(request);

        UserEntity savedEntity = service.createUser(entity);

        UserResponse response = mapper.toResponse(savedEntity);
        log.info("Operatore creato con successo. Database ID: {}, Keycloak ID: {}",
                response.getId(), savedEntity.getKeycloakId());
        return response;
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('OPERATOR')")
    public UserResponse getUser(Long id){
        return mapper.toResponse(service.getUser(id));
    }
}