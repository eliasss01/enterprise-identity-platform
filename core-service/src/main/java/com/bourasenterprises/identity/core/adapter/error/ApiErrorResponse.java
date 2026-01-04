package com.bourasenterprises.identity.core.adapter.error;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String errorMessage;
    private String correlationId;
    private List<ApiErrorDetail> details;
    
}
