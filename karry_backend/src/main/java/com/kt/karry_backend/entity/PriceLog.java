package com.kt.karry_backend.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "price_log")
public class PriceLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricelog_id")
    private Long priceLogId;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "user_id", nullable = false)
    private String userId; 

    @Column(name = "origin", nullable = false, length = 255)
    private String origin;

    @Column(name = "destination", nullable = false, length = 255)
    private String destination;

    @Column(name = "origin_x", precision = 15, scale = 10) // 좌표 추가
    private BigDecimal originX;

    @Column(name = "origin_y", precision = 15, scale = 10)
    private BigDecimal originY;

    @Column(name = "destination_x", precision = 15, scale = 10)
    private BigDecimal destinationX;

    @Column(name = "destination_y", precision = 15, scale = 10)
    private BigDecimal destinationY;

    @Column(name = "distance", precision = 10, scale = 2)
    private BigDecimal distance;

    @Column(name = "simple_price", precision = 10, scale = 2)
    private BigDecimal simplePrice;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "size", precision = 10, scale = 2)
    private BigDecimal size;

    @Column(name = "calculated_price", precision = 10, scale = 2)
    private BigDecimal calculatedPrice;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
