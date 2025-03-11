package com.kt.karry_backend.service;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.karry_backend.domain.FactTableRepository;
import com.kt.karry_backend.entity.FactTable;

@Service
@RequiredArgsConstructor
public class FactTableService {

    @Autowired
    private FactTableRepository factTableRepository;

    @Transactional
    public void migrateDataToFactTable() {
        factTableRepository.migrateFactTable();
    }

    public Map<String, Object> getDashboardData(String userId) {
        
        List<FactTable> userFacts;
        
        boolean isCarrier = factTableRepository.existsByAcceptedBy(userId);
        boolean isShipper = factTableRepository.existsByUserId(userId);
        
        if (isCarrier) {
                userFacts = factTableRepository.findByAcceptedBy(userId);
        } else {
                userFacts = factTableRepository.findByUserId(userId);
        }

        List<FactTable> carrierShipments = isCarrier ? userFacts : List.of(); 
        List<FactTable> shipperShipments = isShipper ? userFacts : List.of();

        // List<FactTable> carrierShipments = userFacts.stream()
        //         .filter(fact -> userId.equals(fact.getAcceptedBy()))
        //         .collect(Collectors.toList());

        // List<FactTable> shipperShipments = userFacts.stream()
        //         .filter(fact -> "shipper".equals(fact.getUserRole()))
        //         .collect(Collectors.toList());

        BigDecimal unsettledAmount = carrierShipments.stream()
                .filter(fact -> Boolean.FALSE.equals(fact.getAdjustmentStatus()))
                .map(fact -> fact.getAmount() != null ? fact.getAmount() : BigDecimal.ZERO) 
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal settledAmount = carrierShipments.stream()
                .filter(fact -> Boolean.TRUE.equals(fact.getAdjustmentStatus()))
                .map(fact -> fact.getAmount() != null ? fact.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPayment = shipperShipments.stream()
                .map(fact -> fact.getAmount() != null ? fact.getAmount() : BigDecimal.ZERO) 
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("carrierShipments", carrierShipments);
        result.put("unsettledAmount", unsettledAmount);
        result.put("settledAmount", settledAmount);
        result.put("shipperShipments", shipperShipments);
        result.put("totalPayment", totalPayment);

        System.out.println("🚀 Carrier Shipments for user " + userId + ": " + carrierShipments);

        return result;
        }
}