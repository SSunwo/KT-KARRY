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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kt.karry_backend.entity.Shipment;
// import com.kt.karry_backend.service.ShipmentRecommendationService;
import com.kt.karry_backend.service.ShipmentService;

@RestController
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    // @Autowired
    // private ShipmentRecommendationService recommendationService;


    // shipment 목록 불러오기
    @GetMapping("/shipment")
    public List<Shipment> getShipmentsAll() {
        return shipmentService.findAll();
    }

    // shipment_id로 조회
    @GetMapping("/shipment/{id}")
    public ResponseEntity<?> getShipment(@PathVariable Long id) {
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

    @PatchMapping("/shipment/{shipmentId}")
    public ResponseEntity<String> updateShipmentStatus(
            @PathVariable Long shipmentId, 
            @RequestBody Map<String, String> request) {

        String newStatus = request.get("status");

        if (newStatus == null || newStatus.isEmpty()) {
            return ResponseEntity.badRequest().body("Status 값이 비어 있습니다.");
        }
        
        boolean updated = shipmentService.updateShipmentStatus(shipmentId, newStatus);
        
        if (updated) {
            return ResponseEntity.ok("Shipment 상태가 '" + newStatus + "'로 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("Shipment 상태 업데이트 실패!");
        }
    }

    // @GetMapping("/shipment/recommend")
    // public List<Shipment> getRecommendedShipments() {
    //     return recommendationService.recommendShipments();
    // }


}
