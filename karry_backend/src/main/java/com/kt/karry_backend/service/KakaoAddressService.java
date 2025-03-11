package com.kt.karry_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.util.KakaoAddressClient;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class KakaoAddressService {

    private final KakaoAddressClient kakaoAddressClient;
    private final String kakaoApiKey = "KakaoAK 6469805a77a32b4e46d9659e4fa2933a"; // ✅ Kakao API Key

    public KakaoAddressService(KakaoAddressClient kakaoAddressClient) {
        this.kakaoAddressClient = kakaoAddressClient;
    }

    public BigDecimal[] getCoordinates(String address) {
        try {
            // 1. Feign Client를 사용하여 API 호출
            ResponseEntity<Map> response = kakaoAddressClient.getCoordinates(address, kakaoApiKey);

            // 2. 응답 데이터 확인
            System.out.println("📌 API 응답: " + response.getBody());

            if (response.getBody() == null || !response.getBody().containsKey("documents")) {
                System.err.println("❌ 응답 데이터가 없습니다.");
                return new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
            }

            // 3. 좌표 추출
            List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

            if (documents.isEmpty()) {
                System.err.println("❌ documents 리스트가 비어 있습니다.");
                return new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
            }

            Map<String, Object> firstResult = documents.get(0);
            BigDecimal x = new BigDecimal(firstResult.get("x").toString());
            BigDecimal y = new BigDecimal(firstResult.get("y").toString());

            System.out.println("🚀 좌표 변환 성공: X=" + x + ", Y=" + y);
            return new BigDecimal[]{x, y};

        } catch (Exception e) {
            System.err.println("❌ 주소 좌표 변환 실패: " + e.getMessage());
        }
        return new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
    }
}