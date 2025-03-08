package com.kt.karry_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.PriceLogRepository;
import com.kt.karry_backend.entity.PriceLog;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceLogService {


    @Autowired
    private PriceLogRepository priceLogRepository;

    public void savePriceLog(PriceLog priceLog) {
        priceLogRepository.save(priceLog);
    }
}