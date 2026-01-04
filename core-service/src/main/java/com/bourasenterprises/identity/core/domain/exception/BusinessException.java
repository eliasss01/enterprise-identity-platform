package com.bourasenterprises.identity.core.domain.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String customMessage;

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.customMessage = errorCode.getDefaultMessage();
    }

    public BusinessException(ErrorCode errorCode, String customMessage){
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

}
