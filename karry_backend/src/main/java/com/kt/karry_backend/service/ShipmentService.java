package com.kt.karry_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.ShipmentRepository;
import com.kt.karry_backend.entity.Shipment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    
    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> findByShipmentId(Integer shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId);
    };

    public Shipment registerShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }
    
}
