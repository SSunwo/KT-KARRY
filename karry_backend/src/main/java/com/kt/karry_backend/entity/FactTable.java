package com.kt.karry_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fact")
public class FactTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fact_id")
    private Long factId;  // Fact 테이블의 고유 ID

    @Column(name = "user_id")
    private String userId;  // 사용자 ID

    @Column(name = "user_role")
    private String userRole;  // 사용자 역할 (shipper/carrier)

    @Column(name = "shipment_id")
    private Long shipmentId;  // 배송 ID

    @Column(name = "shipment_origin")
    private String shipmentOrigin;  // 출발지

    @Column(name = "shipment_destination")
    private String shipmentDestination;  // 도착지

    @Column(name = "shipment_status")
    private String shipmentStatus;  // 배송 상태 (Pending, Shipping, Completed)

    @Column(name = "matching_id")
    private Long matchingId;  // 매칭 ID

    @Column(name = "accepted_by")
    private String acceptedBy;  // 매칭 수락자 (운송 기사 ID)

    @Column(name = "transaction_id")
    private Long transactionId;  // 거래 ID

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;  // 거래 금액

    @Column(name = "adjustment_status")
    private Boolean adjustmentStatus;  // 정산 상태 (true/false)

    @Column(name = "pricelog_id")
    private Long priceLogId;  // 요금 로그 ID

    @Column(name = "calculated_price", precision = 10, scale = 2)
    private BigDecimal calculatedPrice;  // 최종 계산된 가격

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;  // 생성 일시
}