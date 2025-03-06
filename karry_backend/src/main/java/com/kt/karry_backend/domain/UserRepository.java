package com.kt.karry_backend.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kt.karry_backend.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
} 
