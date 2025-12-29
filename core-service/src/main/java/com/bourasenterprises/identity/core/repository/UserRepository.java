package com.bourasenterprises.identity.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bourasenterprises.identity.core.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByEmail(String email);
}
