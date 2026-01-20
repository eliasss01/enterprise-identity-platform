package com.bourasenterprises.identity.core.adapter.error;

import java.time.Instant;
import java.util.List;

import com.bourasenterprises.identity.core.domain.exception.BusinessException;
import com.bourasenterprises.identity.core.domain.exception.ErrorCode;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex){
        ErrorCode ec = ex.getErrorCode();

        ApiErrorResponse errorResponse = baseError(
                ec.getStatus(),
                ec.getCode(),
                ex.getCustomMessage());

        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex){

        List<ApiErrorDetail> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> new ApiErrorDetail(
                err.getField(),
                err.getDefaultMessage()
            ))
            .toList();
        
        ApiErrorResponse error = baseError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Invalid Request");
        error.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ApiErrorResponse error = baseError(
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                "You do not have the required roles to access this resource. Please contact your administrator."
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        ApiErrorResponse error = baseError(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHENTICATED",
                "Full authentication is required to access this resource. Check if your token is missing, invalid or expired."
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex){
        ApiErrorResponse error = baseError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_ERROR", 
            "Unexpected Error"
        );
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);    

    }


    
    private ApiErrorResponse baseError(HttpStatus status, String code, String message){
        ApiErrorResponse error = new ApiErrorResponse();
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError(code);
        error.setErrorMessage(message);
        error.setCorrelationId(MDC.get("correlationId"));
        return error;
    }
    
}
