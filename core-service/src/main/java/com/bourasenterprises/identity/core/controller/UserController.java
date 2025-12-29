package com.bourasenterprises.identity.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.bourasenterprises.identity.core.application.UserApplicationService;
import com.example.generated.api.UsersApi;
import com.example.generated.model.CreateUserRequest;
import com.example.generated.model.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserApplicationService controller;


    @Override
    public ResponseEntity<UserResponse> getUser(Long id) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(controller.getUser(id));
    }


    @Override
    public ResponseEntity<UserResponse> createUser(@Valid CreateUserRequest createUserRequest) throws Exception {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(controller.createUser(createUserRequest));
    }
    
}
