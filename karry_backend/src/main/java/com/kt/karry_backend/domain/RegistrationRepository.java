package com.kt.karry_backend.domain;

import org.springframework.stereotype.Repository;

import com.kt.karry_backend.entity.Registration;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

    
    List<Registration> findAll();
}
