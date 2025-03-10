package com.kt.karry_backend.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.karry_backend.entity.Matching;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    
}
