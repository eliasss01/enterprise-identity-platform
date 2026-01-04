package com.bourasenterprises.identity.core.application;

import org.springframework.stereotype.Service;

import com.bourasenterprises.identity.core.domain.UserEntity;
import com.bourasenterprises.identity.core.mapper.UserMapper;
import com.bourasenterprises.identity.core.service.UserService;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService service;
    private final UserMapper mapper;

    public UserResponse createUser(CreateUserRequest request){
        UserEntity entity = mapper.toEntity(request);
        return mapper.toResponse(service.createUser(entity));
    }

    public UserResponse getUser(Long id){
        return mapper.toResponse(service.getUser(id));
    }
    
}
