package com.bourasenterprises.identity.core.mapper;

import com.bourasenterprises.identity.core.entity.UserEntity;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-29T14:36:22+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (GraalVM Community)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntity(CreateUserRequest request) {
        if ( request == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.email( request.getEmail() );
        userEntity.fullName( request.getFullName() );

        return userEntity.build();
    }

    @Override
    public UserResponse toResponse(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( entity.getId() );
        userResponse.setEmail( entity.getEmail() );
        userResponse.setFullName( entity.getFullName() );
        userResponse.setActive( entity.getActive() );

        return userResponse;
    }
}
