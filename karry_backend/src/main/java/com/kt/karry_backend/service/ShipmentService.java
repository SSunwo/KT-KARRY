package com.kt.karry_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.ShipmentRepository;
import com.kt.karry_backend.entity.Shipment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PriceLogService priceLogService;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> findByShipmentId(Long shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId);
    };

    @Transactional
    public Shipment registerShipment(Shipment shipment) {
        Shipment savedShipment = shipmentRepository.save(shipment);

        // PriceLog 자동 저장
        priceLogService.savePriceLog(savedShipment);

        return savedShipment;
    }

    @Transactional
    public boolean updateShipmentStatus(Long shipmentId, String newStatus) {
        Optional<Shipment> shipmentOptional = shipmentRepository.findByShipmentId(shipmentId);
        
        if (shipmentOptional.isPresent()) {
            Shipment shipment = shipmentOptional.get();
            shipment.setStatus(newStatus);
            shipmentRepository.save(shipment);
            return true;
        } else {
            return false;
        }
    }
}
