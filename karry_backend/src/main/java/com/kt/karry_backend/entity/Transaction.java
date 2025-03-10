package com.kt.karry_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;  // 거래 ID (PK)

    @Column(name = "matching_id", nullable = false)
    private Long matchingId;  // 매칭 ID

    @Column(name = "pricelog_id", nullable = false)
    private Long pricelogId;  // 요금 로그 ID

    @Column(name="created_by", nullable = false)
    private String createdBy; // 등록한 사용자 ID

    @Column(name="accepted_by", nullable = true)
    private String acceptedBy; // 수락한 사용자 ID (기본값 null)

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;  // 거래 금액

    @Column(name = "status", nullable = false, length = 50)
    private String status;  // 결제 상태 (Shipping, Complete)

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 일시

    @Column(name = "adjustment_status", nullable = false)
    private Boolean adjustmentStatus;  // 정산 상태 (false = 미정산, true = 정산 완료)
}