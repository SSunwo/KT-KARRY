// package com.kt.karry_backend;

// import static org.junit.jupiter.api.Assertions.*;

// import com.kt.karry_backend.service.KakaoAddressService;
// import com.kt.karry_backend.util.KakaoAddressClient;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.ResponseEntity;
// import java.math.BigDecimal;
// import java.util.List;
// import java.util.Map;

// @SpringBootTest
// public class KakaoAddressServiceTest {

//     private final KakaoAddressClient mockKakaoClient = new KakaoAddressClient() {
//         @Override
//         public ResponseEntity<Map> getCoordinates(String address, String apiKey) {
//             return ResponseEntity.ok(Map.of(
//                 "documents", List.of(Map.of(
//                     "x", "127.123456",
//                     "y", "37.987654"
//                 ))
//             ));
//         }
//     };

//     private final KakaoAddressService addressService = new KakaoAddressService(mockKakaoClient);

//     @Test
//     public void testGetCoordinates() {
//         BigDecimal[] coords = addressService.getCoordinates("서울 강남구 테헤란로 427");

//         assertNotNull(coords, "❌ 좌표 배열이 null입니다.");
//         assertTrue(coords[0].compareTo(BigDecimal.ZERO) != 0, "❌ X 좌표가 0입니다.");
//         assertTrue(coords[1].compareTo(BigDecimal.ZERO) != 0, "❌ Y 좌표가 0입니다.");

//         System.out.println("✅ 좌표 변환 성공: X=" + coords[0] + ", Y=" + coords[1]);
//     }
// }