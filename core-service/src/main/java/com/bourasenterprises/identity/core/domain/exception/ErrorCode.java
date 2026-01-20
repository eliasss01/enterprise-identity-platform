package com.bourasenterprises.identity.core.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("USER_001", "Utente non trovato", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("USER_002", "Email già registrata", HttpStatus.CONFLICT),
    INVALID_PERMISSION("AUTH_001", "Permessi insufficienti", HttpStatus.FORBIDDEN),
    INTERNAL_ERROR("AUTH_002", "Errore durante la creazione su Keycloak", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

}
