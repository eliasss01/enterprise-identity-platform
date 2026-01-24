package com.bourasenterprises.identity.core.service;

import com.bourasenterprises.identity.core.domain.exception.BusinessException;
import com.bourasenterprises.identity.core.domain.exception.ErrorCode;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bourasenterprises.identity.core.domain.UserEntity;
import com.bourasenterprises.identity.core.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final Keycloak keycloak;

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserEntity createUser(UserEntity userRequest){

        repository.findByEmail(userRequest.getEmail())
                .ifPresent(u -> {
                    throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email già presente");
                });

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userRequest.getEmail());
        kcUser.setEmail(userRequest.getEmail());
        kcUser.setFirstName(userRequest.getFullName());
        kcUser.setEnabled(true);

        Response response = keycloak.realm("enterprise-platform").users().create(kcUser);

        if (response.getStatus() != 201) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Errore durante la creazione su Keycloak");
        }

        String keycloakId = CreatedResponseUtil.getCreatedId(response);

        UserEntity userEntity = UserEntity.builder()
                .keycloakId(java.util.UUID.fromString(keycloakId))
                .email(userRequest.getEmail())
                .fullName(userRequest.getFullName())
                .role("OPERATOR")
                .active(true)
                .build();

        return repository.save(userEntity);
    }

    @Cacheable(value = "users", key = "#id")
    public UserEntity getUser(Long id){
        System.out.println("DB HIT for user " + id); // per vedere quando usa Redis vs DB

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "ID non trovato: " + id));
    }
}
