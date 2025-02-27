package com.kt.karry_backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.kt.karry_backend.entity.Registration;
import com.kt.karry_backend.service.RegistrationService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class RegistrationsController {
    
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/regists")
    public List<Registration> getRegistrationsAll() {
        return registrationService.findAll();
    }

    @PostMapping("/regists")
    public ResponseEntity<?> postRegistration(@RequestBody Registration resource) throws URISyntaxException {
    
        Registration registration = registrationService.addRegistration(resource);

        URI location = URI.create("/regists/" + registration.getRegist_num());
        return ResponseEntity.created(location).body("creation complete");
    }
    

}
