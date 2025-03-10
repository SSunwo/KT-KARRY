package com.kt.karry_backend.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.karry_backend.entity.Matching;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
    List<Matching> findByUserId(String userId);

    List<Matching> findByCreatedBy(String createdBy);
    List<Matching> findByAcceptedBy(String acceptedBy);
    
    Optional<Matching> findByMatchingId(Long matchingId);
}
