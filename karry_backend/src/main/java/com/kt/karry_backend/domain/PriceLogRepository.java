package com.kt.karry_backend.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.karry_backend.entity.PriceLog;

@Repository
public interface PriceLogRepository extends JpaRepository<PriceLog, Long> {
    
    Optional<PriceLog> findByShipmentId(Long shipmentId);
}
