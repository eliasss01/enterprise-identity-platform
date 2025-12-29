package com.bourasenterprises.identity.core.service;

import org.springframework.stereotype.Service;

import com.bourasenterprises.identity.core.entity.UserEntity;
import com.bourasenterprises.identity.core.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public UserEntity createUser(UserEntity userRequest){

        repository.findByEmail(userRequest.getEmail())
            .ifPresent(u -> {
                throw new IllegalArgumentException("User already registered");
            });
        
        UserEntity userEntity = UserEntity.builder()
            .email(userRequest.getEmail())
            .fullName(userRequest.getFullName())
            .build();
        
        return repository.save(userEntity);
    }

    public UserEntity getUser(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    
}
