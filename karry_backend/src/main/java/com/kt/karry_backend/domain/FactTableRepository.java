package com.kt.karry_backend.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.kt.karry_backend.entity.FactTable;

public interface FactTableRepository extends JpaRepository<FactTable, Long> {

    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO fact (user_id, user_role, shipment_id, shipment_origin, shipment_destination, shipment_status, 
                          matching_id, accepted_by, transaction_id, amount, adjustment_status, pricelog_id, calculated_price, created_at)
        SELECT 
            s.user_id, 
            u.role AS user_role, 
            s.shipment_id, 
            s.origin AS shipment_origin, 
            s.destination AS shipment_destination, 
            s.status AS shipment_status, 
            m.matching_id, 
            m.accepted_by, 
            t.transaction_id, 
            t.amount, 
            t.adjustment_status, 
            p.pricelog_id, 
            p.calculated_price, 
            s.created_at
        FROM shipment s
        LEFT JOIN "user" u ON s.user_id = u.user_id
        LEFT JOIN matching m ON s.shipment_id = m.shipment_id
        LEFT JOIN transaction t ON m.matching_id = t.matching_id
        LEFT JOIN price_log p ON s.shipment_id = p.shipment_id
        """, nativeQuery = true)
    void migrateFactTable();

    List<FactTable> findByUserId(String userId);

    List<FactTable> findByAcceptedBy(String acceptedBy); 

    boolean existsByAcceptedBy(String acceptedBy); 
    boolean existsByUserId(String userId);
}