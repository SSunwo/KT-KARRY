package com.kt.karry_backend.controller;

import java.math.BigDecimal;
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

import com.kt.karry_backend.entity.Transaction;
import com.kt.karry_backend.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

     // 특정 사용자가 등록한 거래 내역 조회
    @GetMapping("/createdBy/{createdBy}")
    public ResponseEntity<?> getTransactionsByCreatedBy(@PathVariable String createdBy) {
        return ResponseEntity.ok(transactionService.getTransactionsByCreatedBy(createdBy));
    }

    // 특정 사용자가 수락한 거래 내역 조회
    @GetMapping("/acceptedBy/{acceptedBy}")
    public ResponseEntity<?> getTransactionsByAcceptedBy(@PathVariable String acceptedBy) {
        return ResponseEntity.ok(transactionService.getTransactionsByAcceptedBy(acceptedBy));
    }

    // 거래 생성 API
    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Map<String, Object> request) {
        Long matchingId = Long.valueOf(request.get("matchingId").toString());
        Long pricelogId = Long.valueOf(request.get("pricelogId").toString());
        String createdBy = request.get("createdBy").toString();
        String acceptedBy = request.get("acceptedBy").toString();
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        Transaction transaction = transactionService.createTransaction(matchingId, pricelogId, createdBy, acceptedBy, amount);
        return ResponseEntity.ok(transaction);
    }

    // 거래 완료 (정산 처리) API
    @PatchMapping("/complete/{transactionId}")
    public ResponseEntity<String> completeTransaction(@PathVariable Long transactionId) {
        boolean isUpdated = transactionService.completeTransaction(transactionId);
        if (isUpdated) {
            return ResponseEntity.ok("✅ 거래가 정산 완료되었습니다!");
        } else {
            return ResponseEntity.badRequest().body("🚨 거래 정산 실패! 존재하지 않는 거래 ID입니다.");
        }
    }

    // 매칭 ID로 거래 ID 조회 API
    @GetMapping("/matching/{matchingId}")
    public ResponseEntity<?> getTransactionIdByMatchingId(@PathVariable Long matchingId) {
        Transaction transaction = transactionService.getTransactionByMatchingId(matchingId);
        
        if (transaction == null) {
            return ResponseEntity.badRequest().body("해당 매칭 ID의 거래를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(Map.of("transactionId", transaction.getTransactionId()));
    }

}
