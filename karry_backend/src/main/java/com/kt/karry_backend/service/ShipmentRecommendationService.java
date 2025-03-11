// package com.kt.karry_backend.service;

// import com.kt.karry_backend.domain.FactTableRepository;
// import com.kt.karry_backend.domain.ShipmentRepository;
// import com.kt.karry_backend.entity.FactTable;
// import com.kt.karry_backend.entity.Shipment;
// import org.springframework.stereotype.Service;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// @Service
// public class ShipmentRecommendationService {

//     private final FactTableRepository factTableRepository;
//     private final ShipmentRepository shipmentRepository;
//     private final OpenAIService openAIService;

//     public ShipmentRecommendationService(FactTableRepository factTableRepository, ShipmentRepository shipmentRepository, OpenAIService openAIService) {
//         this.factTableRepository = factTableRepository;
//         this.shipmentRepository = shipmentRepository;
//         this.openAIService = openAIService;
//     }

//     public List<Shipment> recommendShipments() {
//         List<FactTable> factData = factTableRepository.findAll();
//         List<Shipment> allShipments = shipmentRepository.findAll();

//         System.out.println(factData);
//         System.out.println(allShipments);

//         List<Map<String, Object>> factDataList = factData.stream().map(fact -> {
//             Map<String, Object> map = new HashMap<>();
//             map.put("shipmentId", fact.getShipmentId());
//             map.put("userRole", fact.getUserRole());
//             map.put("shipmentStatus", fact.getShipmentStatus());
//             map.put("amount", fact.getAmount());
//             map.put("calculatedPrice", fact.getCalculatedPrice());
//             return map;
//         }).collect(Collectors.toList());

//         List<Map<String, Object>> shipmentList = allShipments.stream().map(shipment -> {
//             Map<String, Object> map = new HashMap<>();
//             map.put("shipmentId", shipment.getShipmentId());
//             map.put("origin", shipment.getOrigin());
//             map.put("destination", shipment.getDestination());
//             map.put("status", shipment.getStatus());
//             map.put("weight", shipment.getWeight());
//             map.put("size", shipment.getSize());
//             return map;
//         }).collect(Collectors.toList());

//         String response = openAIService.getRecommendation(factDataList, shipmentList);
//         System.out.println("🚀 AI 추천 결과: " + response);

//         List<Long> recommendedShipmentIds = parseAIResponse(response);

//         return allShipments.stream()
//                 .sorted((s1, s2) -> {
//                     int index1 = recommendedShipmentIds.indexOf(s1.getShipmentId());
//                     int index2 = recommendedShipmentIds.indexOf(s2.getShipmentId());
//                     return Integer.compare(index1, index2);
//                 })
//                 .collect(Collectors.toList());
//     }

//     private List<Long> parseAIResponse(String aiResponse) {
//         return List.of(); // JSON 파싱 로직 추가 필요
//     }
// }
