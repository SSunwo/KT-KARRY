package com.kt.karry_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.karry_backend.domain.TransactionRepository;
import com.kt.karry_backend.entity.Transaction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // 거래 생성
    @Transactional
    public Transaction createTransaction(Long matchingId, Long pricelogId, String createdBy, String acceptedBy, BigDecimal amount) {
        Transaction transaction = Transaction.builder()
                .matchingId(matchingId)
                .pricelogId(pricelogId)
                .createdBy(createdBy)
                .acceptedBy(acceptedBy)
                .amount(amount)
                .status("Shipping")
                .adjustmentStatus(false)
                .build();

        return transactionRepository.save(transaction);
    }

    // 정산 상태 업데이트 (배송 완료 시)
    @Transactional
    public boolean completeTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(transaction -> {
                    transaction.setStatus("Complete");
                    transaction.setAdjustmentStatus(true);
                    transactionRepository.save(transaction);
                    return true;
                }).orElse(false);
    }

    // 특정 사용자가 생성한 거래 내역 조회
    public List<Transaction> getTransactionsByCreatedBy(String createdBy) {
        return transactionRepository.findByCreatedBy(createdBy);
    }

    // 특정 사용자가 수락한 거래 내역 조회
    public List<Transaction> getTransactionsByAcceptedBy(String acceptedBy) {
        return transactionRepository.findByAcceptedBy(acceptedBy);
    }

    public Transaction getTransactionByMatchingId(Long matchingId) {
        return transactionRepository.findByMatchingId(matchingId).orElse(null);
    }

    public BigDecimal getUnsettledAmountByCarrier(String userId) {
        return transactionRepository.getUnsettledAmountByCarrier(userId);
    }

    public BigDecimal getSettledAmountByCarrier(String userId) {
        return transactionRepository.getSettledAmountByCarrier(userId);
    }

    public List<Transaction> getPendingShipmentsByShipper(String userId) {
        return transactionRepository.getPendingShipmentsByShipper(userId);
    }

    public BigDecimal getTotalPaymentByShipper(String userId) {
        return transactionRepository.getTotalPaymentByShipper(userId);
    }
}
