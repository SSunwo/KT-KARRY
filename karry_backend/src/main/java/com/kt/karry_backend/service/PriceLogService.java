package com.kt.karry_backend.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.PriceLogRepository;
import com.kt.karry_backend.entity.PriceLog;
import com.kt.karry_backend.entity.Shipment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceLogService {

    @Autowired
    private PriceLogRepository priceLogRepository;

    @Autowired
    private KakaoAddressService addressService;

    @Autowired
    private NaverMapService naverMapService;

    public void savePriceLog(Shipment shipment) {
        // 출발지, 도착지의 좌표 변환
        BigDecimal[] originCoordinates = addressService.getCoordinates(shipment.getOrigin());
        BigDecimal[] destinationCoordinates = addressService.getCoordinates(shipment.getDestination());

        // 네이버 API를 사용한 실제 거리 및 예상 요금 가져오기
        BigDecimal actualDistance = naverMapService.getDistance(originCoordinates, destinationCoordinates);
        BigDecimal taxiFare = naverMapService.getTaxiFare(originCoordinates, destinationCoordinates);
        // 요금 계산 (예제)
        BigDecimal weightCharge = shipment.getWeight().multiply(BigDecimal.valueOf(300)); // 1kg당 300원
        BigDecimal sizeCharge = shipment.getSize().multiply(BigDecimal.valueOf(200)); // 1m³당 200원
        BigDecimal calculatedPrice = taxiFare.add(weightCharge).add(sizeCharge);

        // PriceLog 생성 및 저장
        PriceLog priceLog = PriceLog.builder()
                .shipmentId(shipment.getShipmentId().longValue())
                .userId(shipment.getUserId())
                .origin(shipment.getOrigin())
                .destination(shipment.getDestination())
                .originX(originCoordinates[0])
                .originY(originCoordinates[1])
                .destinationX(destinationCoordinates[0])
                .destinationY(destinationCoordinates[1])
                .distance(actualDistance)
                .simplePrice(taxiFare) // 네이버 API의 예상 택시 요금
                .weight(shipment.getWeight())
                .size(shipment.getSize())
                .calculatedPrice(calculatedPrice)
                .build();

        priceLogRepository.save(priceLog);
        System.out.println("PriceLog 저장 완료: " + priceLog);
    }

    public Optional<PriceLog> getPriceLogByShipmentId(Long shipmentId) {
        return priceLogRepository.findByShipmentId(shipmentId);
    }
}