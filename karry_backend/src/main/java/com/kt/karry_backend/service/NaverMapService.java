package com.kt.karry_backend.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.util.NaverMapClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class NaverMapService {

    private final NaverMapClient naverMapClient;

    public NaverMapService(NaverMapClient naverMapClient) {
        this.naverMapClient = naverMapClient;
    }

    public BigDecimal getDistance(BigDecimal[] start, BigDecimal[] goal) {
        try {
            // ✅ API 호출
            ResponseEntity<Map> response = naverMapClient.getRoute(start[0] + "," + start[1], goal[0] + "," + goal[1]);

            if (response.getBody() == null || !response.getBody().containsKey("route")) {
                System.err.println("❌ 네이버 지도 API 응답 없음");
                return BigDecimal.ZERO;
            }

            // ✅ 응답 데이터 추출
            Map<String, Object> route = (Map<String, Object>) response.getBody().get("route");
            Map<String, Object> traoptimal = (Map<String, Object>) ((java.util.List<?>) route.get("traoptimal")).get(0);
            Map<String, Object> summary = (Map<String, Object>) traoptimal.get("summary");

            // ✅ 거리 추출 (단위: m -> km 변환)
            int distanceMeters = (int) summary.get("distance");
            BigDecimal distanceKm = BigDecimal.valueOf(distanceMeters / 1000.0);

            System.out.println("🚗 총 거리: " + distanceKm + " km");
            return distanceKm;

        } catch (Exception e) {
            System.err.println("❌ 네이버 지도 API 호출 실패: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTaxiFare(BigDecimal[] start, BigDecimal[] goal) {
        try {
            // ✅ API 호출
            ResponseEntity<Map> response = naverMapClient.getRoute(start[0] + "," + start[1], goal[0] + "," + goal[1]);

            if (response.getBody() == null || !response.getBody().containsKey("route")) {
                System.err.println("❌ 네이버 지도 API 응답 없음");
                return BigDecimal.ZERO;
            }

            // ✅ 응답 데이터 추출
            Map<String, Object> route = (Map<String, Object>) response.getBody().get("route");
            Map<String, Object> traoptimal = (Map<String, Object>) ((java.util.List<?>) route.get("traoptimal")).get(0);
            Map<String, Object> summary = (Map<String, Object>) traoptimal.get("summary");

            // ✅ 택시 요금 추출
            int taxiFare = (int) summary.get("taxiFare");
            BigDecimal fare = BigDecimal.valueOf(taxiFare);

            System.out.println("🚕 예상 택시 요금: " + fare + " 원");
            return fare;

        } catch (Exception e) {
            System.err.println("❌ 네이버 지도 API 호출 실패: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}