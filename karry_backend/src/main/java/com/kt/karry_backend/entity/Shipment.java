package com.kt.karry_backend.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import com.kt.karry_backend.service.PriceLogService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name="shipment")
public class Shipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shipment_id")
    private Integer shipmentId;

    @Column(name="user_id")
    private String userId;

    @Column(name="origin")
    private String origin;
    @Column(name="destination")
    private String destination;

    @Column(name="weight", precision = 10, scale = 2)
    private BigDecimal weight;
    @Column(name="size", precision = 10, scale = 2)
    private BigDecimal size;
    @Column(name="price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name="status")
    private String status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    // @Transient
    // private static PriceLogService priceLogService; // Service 주입

    // @Autowired
    // public void setPriceLogService(PriceLogService service) {
    //     priceLogService = service;
    // }

    // // data 저장되면 price log에도 저장
    // @PostPersist
    // public void afterInsert() {
    //     System.out.println("🚀 Shipment Inserted! Creating PriceLog...");

    //     // 거리 및 요금 계산 로직 (API 또는 간단한 공식을 적용)
    //     BigDecimal distance = calculateDistance(origin, destination);
    //     BigDecimal simplePrice = distance.multiply(BigDecimal.valueOf(1000)); // 예제 요금 계산
    //     BigDecimal calculatedPrice = simplePrice.add(weight.multiply(BigDecimal.valueOf(500)));

    //     PriceLog priceLog = PriceLog.builder()
    //             .shipmentId(this.shipmentId.longValue())
    //             .origin(this.origin)
    //             .destination(this.destination)
    //             .distance(distance)
    //             .simplePrice(simplePrice)
    //             .weight(this.weight)
    //             .size(this.size)
    //             .calculatedPrice(calculatedPrice)
    //             .build();

    //     priceLogService.savePriceLog(priceLog); // 서비스 호출하여 저장
    // }

    // private BigDecimal calculateDistance(String origin, String destination) {
    //     // 네이버 지도 API 또는 간단한 거리 계산 방식 적용
    //     return BigDecimal.valueOf(10.5); // 예제: 10.5km
    // }
}
