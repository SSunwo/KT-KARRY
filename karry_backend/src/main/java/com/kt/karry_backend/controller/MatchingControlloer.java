package com.kt.karry_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.karry_backend.entity.Matching;
import com.kt.karry_backend.service.MatchingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingControlloer {
    
    @Autowired
    private MatchingService matchingService;

    // 특정 userId에 해당하는 Matching 목록 조회 API
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMatchings(@PathVariable String userId) {
        List<Matching> matchings = matchingService.getMatchingsByUserId(userId);
        return ResponseEntity.ok(matchings);
    }


    @PostMapping("/{shipmentId}")
    public ResponseEntity<Matching> assignMatching(@PathVariable Long shipmentId) {
        
        Matching matching = matchingService.createMatching(shipmentId);
        return ResponseEntity.ok(matching);
    }

    // 매칭 상태 변경 (Completed 또는 Cancelled)
    @PatchMapping("/{matchingId}")
    public ResponseEntity<String> updateMatchingStatus(
            @PathVariable Long matchingId, 
            @RequestBody Map<String, String> request) {

        String newStatus = request.get("status");
        boolean updated = matchingService.updateMatchingStatus(matchingId, newStatus);

        if (updated) {
            return ResponseEntity.ok("Matching 상태가 '" + newStatus + "'로 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("Matching 상태 업데이트 실패!");
        }
    }
}
