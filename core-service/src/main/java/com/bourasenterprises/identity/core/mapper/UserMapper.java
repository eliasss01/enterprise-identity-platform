package com.bourasenterprises.identity.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.bourasenterprises.identity.core.domain.UserEntity;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // L'id è autoincrementale del DB, il keycloakId lo mettiamo noi nel Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    // Ignoriamo i campi che non arrivano dal frontend
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    UserEntity toEntity(CreateUserRequest request);

    UserResponse toResponse(UserEntity entity);
}