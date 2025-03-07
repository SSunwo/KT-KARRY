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

    @Column(name="user_num")
    private String userNum;
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
}
