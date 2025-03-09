package com.kt.karry_backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kt.karry_backend.entity.PriceLog;
import com.kt.karry_backend.service.PriceLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pricelog")
@RequiredArgsConstructor
public class PriceLogController {
    
    @Autowired
    private PriceLogService priceLogService;
    
    // 특정 Shipment ID에 대한 PriceLog 조회 API
    @GetMapping("/{shipmentId}")
    public ResponseEntity<?> getPriceLogByShipmentId(@PathVariable Long shipmentId) {
        
        // Optional<PriceLog> priceLog = priceLogService.getPriceLogByShipmentId(shipmentId);
        PriceLog priceLog = priceLogService.getPriceLogByShipmentId(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found")) ;

        return ResponseEntity.ok(priceLog);
    }
}
