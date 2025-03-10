package com.kt.karry_backend.domain;

import com.kt.karry_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // // 특정 매칭 ID로 거래 찾기
    // List<Transaction> findByMatchingId(Long matchingId);

    // 정산이 완료되지 않은 거래 찾기
    List<Transaction> findByAdjustmentStatusFalse();

    // 특정 사용자 ID가 등록한 거래 조회
    List<Transaction> findByCreatedBy(String createdBy);

    // 특정 사용자 ID가 수락한 거래 조회
    List<Transaction> findByAcceptedBy(String acceptedBy);

    Optional<Transaction> findByMatchingId(Long matchingId);
}