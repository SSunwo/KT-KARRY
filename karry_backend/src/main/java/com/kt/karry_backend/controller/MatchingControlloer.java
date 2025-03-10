package com.kt.karry_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.karry_backend.entity.Matching;
import com.kt.karry_backend.service.MatchingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingControlloer {
    
    @Autowired
    private MatchingService matchingService;

    @PostMapping("/{shipmentId}")
    public ResponseEntity<Matching> assignMatching(@PathVariable Long shipmentId) {
        
        Matching matching = matchingService.createMatching(shipmentId);
        return ResponseEntity.ok(matching);
    }
}
