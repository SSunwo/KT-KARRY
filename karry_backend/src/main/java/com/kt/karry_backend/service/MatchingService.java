package com.kt.karry_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.MatchingRepository;
import com.kt.karry_backend.domain.PriceLogRepository;
import com.kt.karry_backend.entity.Matching;
import com.kt.karry_backend.entity.PriceLog;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchingService {
    
    @Autowired
    private MatchingRepository matchingRepository;

    @Autowired
    private PriceLogRepository priceLogRepository;


    // 특정 userId의 Matching 조회
    public List<Matching> getMatchingsByUserId(String userId) {
        return matchingRepository.findByUserId(userId);
    }

    @Transactional
    public Matching createMatching(Long shipmentId) {
        // PriceLog에서 해당 shipmentId의 정보 조회
        PriceLog priceLog = priceLogRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new RuntimeException("해당 Shipment에 대한 PriceLog를 찾을 수 없습니다."));

        // Matching 생성 및 저장
        Matching matching = Matching.builder()
                .shipmentId(priceLog.getShipmentId())
                .userId(priceLog.getUserId())
                .origin(priceLog.getOrigin())
                .destination(priceLog.getDestination())
                .distance(priceLog.getDistance())
                .amount(priceLog.getCalculatedPrice()) // 계산된 요금 적용
                .status("shipping") // 기본 상태 설정
                .acceptedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return matchingRepository.save(matching);
    }

    // 매칭 상태 업데이트 (Completed 또는 Cancelled)
    public boolean updateMatchingStatus(Long matchingId, String newStatus) {
        Optional<Matching> matchingOptional = matchingRepository.findByMatchingId(matchingId);

        if (matchingOptional.isPresent()) {
            Matching matching = matchingOptional.get();
            matching.setStatus(newStatus);
            matchingRepository.save(matching);
            return true;
        }
        return false;
    }
}
