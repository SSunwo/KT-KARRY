package com.kt.karry_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "matching")
public class Matching {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="matching_id", nullable = false)
    private Long matchingId;

    @Column(name="shipment_id", nullable = false)
    private Long shipmentId;

    // @Column(name="user_id", nullable = false)
    // private String userId;
    @Column(name="created_by", nullable = false)
    private String createdBy; // 🚀 등록한 사용자 ID

    @Column(name="accepted_by", nullable = true)
    private String acceptedBy; // 🚚 수락한 사용자 ID (기본값 null)

    @Column(name = "origin", nullable = false, length = 255)
    private String origin;

    @Column(name = "destination", nullable = false, length = 255)
    private String destination;

    @Column(name = "distance", precision = 10, scale = 2)
    private BigDecimal distance;

    // 거래 금액
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status = "shipping"; // 기본 상태를 shipping으로 설정

    @Column(name = "accepted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime acceptedAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}
