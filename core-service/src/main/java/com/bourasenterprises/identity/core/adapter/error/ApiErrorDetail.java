package com.bourasenterprises.identity.core.adapter.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDetail {

    private String field;
    private String issue;

}
