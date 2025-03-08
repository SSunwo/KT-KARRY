package com.kt.karry_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.ShipmentRepository;
import com.kt.karry_backend.entity.PriceLog;
import com.kt.karry_backend.entity.Shipment;

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

    public Optional<Shipment> findByShipmentId(Integer shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId);
    };

    public Shipment registerShipment(Shipment shipment) {
        Shipment savedShipment = shipmentRepository.save(shipment);

        // ✅ Shipment 저장 후 PriceLog 자동 생성
        savePriceLogForShipment(savedShipment);

        return savedShipment;
    }

    private void savePriceLogForShipment(Shipment shipment) {
        BigDecimal distance = calculateDistance(shipment.getOrigin(), shipment.getDestination());
        BigDecimal simplePrice = distance.multiply(BigDecimal.valueOf(1000)); // 예제: 1km당 1000원
        BigDecimal calculatedPrice = simplePrice.add(shipment.getWeight().multiply(BigDecimal.valueOf(500)));

        PriceLog priceLog = PriceLog.builder()
                .shipmentId(shipment.getShipmentId().longValue())
                .origin(shipment.getOrigin())
                .destination(shipment.getDestination())
                .distance(distance)
                .simplePrice(simplePrice)
                .weight(shipment.getWeight())
                .size(shipment.getSize())
                .calculatedPrice(calculatedPrice)
                .build();

        priceLogService.savePriceLog(priceLog);
    }

    private BigDecimal calculateDistance(String origin, String destination) {
        // TODO: 네이버 지도 API 또는 간단한 거리 계산 방식 적용
        return BigDecimal.valueOf(10.5); // 예제: 10.5km
    }
    
}
