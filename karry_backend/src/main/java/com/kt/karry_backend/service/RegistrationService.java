package com.kt.karry_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.RegistrationRepository;
import com.kt.karry_backend.entity.Registration;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    public List<Registration> findAll() {
        return registrationRepository.findAll();
    }

    public Registration addRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }
}
