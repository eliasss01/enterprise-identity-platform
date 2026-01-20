package com.bourasenterprises.identity.core.service;

import com.bourasenterprises.identity.core.domain.exception.BusinessException;
import com.bourasenterprises.identity.core.domain.exception.ErrorCode;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
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
    public UserEntity createUser(UserEntity userRequest){

        repository.findByEmail(userRequest.getEmail())
                .ifPresent(u -> {
                    throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email già presente");
                });

        // CREAZIONE SU KEYCLOAK
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userRequest.getEmail());
        kcUser.setEmail(userRequest.getEmail());
        kcUser.setFirstName(userRequest.getFullName());
        kcUser.setEnabled(true);

        // TODO: Qui andrebbe aggiunta la gestione password (es. invio email reset o password provvisoria)

        Response response = keycloak.realm("enterprise-platform").users().create(kcUser);

        if (response.getStatus() != 201) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Errore durante la creazione su Keycloak");
        }

        // RECUPERO UUID DA KEYCLOAK
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

    public UserEntity getUser(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "ID non trovato: " + id));
    }
}
