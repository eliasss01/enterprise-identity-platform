package com.bourasenterprises.identity.core.mapper;

import org.mapstruct.Mapper;

import com.bourasenterprises.identity.core.domain.UserEntity;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(CreateUserRequest request);

    UserResponse toResponse(UserEntity entity);
    
}
