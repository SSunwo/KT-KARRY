package com.kt.karry_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kt.karry_backend.service.FactTableService;

@RestController
@RequestMapping("/fact")
@RequiredArgsConstructor
public class FactTableController {

    private final FactTableService factTableService;

    @PostMapping("/migrate")
    public ResponseEntity<String> migrateFactData() {
        factTableService.migrateDataToFactTable();
        return ResponseEntity.ok("FactTable 데이터 마이그레이션 완료!");
    }

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<?> getDashboardData(@PathVariable String userId) {
        
        return ResponseEntity.ok(factTableService.getDashboardData(userId));
    }
}