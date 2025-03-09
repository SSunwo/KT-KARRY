package com.kt.karry_backend.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kt.karry_backend.entity.Shipment;
import com.kt.karry_backend.service.ShipmentService;

@RestController
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;


    // shipment 목록 불러오기
    @GetMapping("/shipment")
    public List<Shipment> getShipmentsAll() {
        return shipmentService.findAll();
    }

    // shipment_id로 조회
    @GetMapping("/shipment/{id}")
    public ResponseEntity<?> getShipment(@PathVariable Integer id) {
        Shipment shipment = shipmentService.findByShipmentId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));
        return ResponseEntity.ok(shipment);
    }

    // shipment 등록하기
    @PostMapping("/shipment")
    public ResponseEntity<?> postShipment(@RequestBody Shipment resource) throws URISyntaxException {
    
        Shipment shipment = shipmentService.registerShipment(resource);

        URI.create("/shipment/" + shipment.getShipmentId());
        
        // 성공하면
        Map<String, Object> response = new HashMap<>();
        response.put("message", "shipment successful");
        response.put("shipment_id", shipment.getShipmentId());
        // response.put("location", location);

        return ResponseEntity.ok(response);

    }
}
