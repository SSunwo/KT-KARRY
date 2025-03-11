package com.kt.karry_backend.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.karry_backend.util.NaverMapClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaverMapService {

    private final NaverMapClient naverMapClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NaverMapService(NaverMapClient naverMapClient) {
        this.naverMapClient = naverMapClient;
    }

    public Map<String, Object> getRouteData(BigDecimal[] start, BigDecimal[] goal) {
        Map<String, Object> result = new HashMap<>();

        try {
            // ✅ 네이버 지도 API 호출
            ResponseEntity<Map> response = naverMapClient.getRoute(start[0] + "," + start[1], goal[0] + "," + goal[1]);

            if (response.getBody() == null || !response.getBody().containsKey("route")) {
                System.err.println("❌ 네이버 지도 API 응답 없음");
                return result;
            }

            // ✅ 응답 데이터 추출
            Map<String, Object> route = (Map<String, Object>) response.getBody().get("route");
            Map<String, Object> traoptimal = (Map<String, Object>) ((List<?>) route.get("traoptimal")).get(0);
            Map<String, Object> summary = (Map<String, Object>) traoptimal.get("summary");

            // ✅ 거리 (m → km 변환)
            int distanceMeters = (int) summary.get("distance");
            BigDecimal distanceKm = BigDecimal.valueOf(distanceMeters / 1000.0);

            // ✅ 비용 데이터 추출
            BigDecimal taxiFare = BigDecimal.valueOf((int) summary.get("taxiFare"));
            BigDecimal tollFare = BigDecimal.valueOf((int) summary.get("tollFare"));
            BigDecimal fuelPrice = BigDecimal.valueOf((int) summary.get("fuelPrice"));

            // ✅ 경로 데이터 (JSON 변환)
            List<List<Double>> path = (List<List<Double>>) traoptimal.get("path");
            String routePathJson = objectMapper.writeValueAsString(path);

            // ✅ 결과 맵에 데이터 저장
            result.put("distance", distanceKm);
            result.put("taxiFare", taxiFare);
            result.put("tollFare", tollFare);
            result.put("fuelPrice", fuelPrice);
            result.put("routePath", routePathJson);

            System.out.println("🚗 총 거리: " + distanceKm + " km");
            System.out.println("🚕 예상 택시 요금: " + taxiFare + " 원");
            System.out.println("🛣️ 경로 데이터 저장 완료");

        } catch (JsonProcessingException e) {
            System.err.println("❌ 경로 데이터 JSON 변환 실패: " + e.getMessage());
            result.put("routePath", "[]"); // JSON 변환 실패 시 빈 배열 저장
        } catch (Exception e) {
            System.err.println("❌ 네이버 지도 API 호출 실패: " + e.getMessage());
        }

        return result;
    }
    
}