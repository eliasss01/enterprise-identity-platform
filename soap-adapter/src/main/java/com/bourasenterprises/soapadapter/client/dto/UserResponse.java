package com.bourasenterprises.soapadapter.client.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String email;

    private String fullName;

    private Boolean active;

}
