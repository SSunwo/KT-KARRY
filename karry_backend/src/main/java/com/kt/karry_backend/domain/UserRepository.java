package com.kt.karry_backend.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.karry_backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAll();
    Optional<User> findByUserId(String userId);
    
} 
