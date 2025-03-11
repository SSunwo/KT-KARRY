package com.kt.karry_backend.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.karry_backend.domain.FactTableRepository;

@Service
@RequiredArgsConstructor
public class FactTableService {

    @Autowired
    private FactTableRepository factTableRepository;

    @Transactional
    public void migrateDataToFactTable() {
        factTableRepository.migrateFactTable();
    }
}