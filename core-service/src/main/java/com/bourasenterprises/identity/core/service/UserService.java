package com.bourasenterprises.identity.core.service;

import com.bourasenterprises.identity.core.domain.exception.BusinessException;
import com.bourasenterprises.identity.core.domain.exception.ErrorCode;
import org.springframework.stereotype.Service;

import com.bourasenterprises.identity.core.domain.UserEntity;
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
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "La mail " + u.getEmail() + " è già registrata");
            });
        
        UserEntity userEntity = UserEntity.builder()
            .email(userRequest.getEmail())
            .fullName(userRequest.getFullName())
            .build();
        
        return repository.save(userEntity);
    }

    public UserEntity getUser(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "Utente non trovato con ID: " + id));
    }
    
}
