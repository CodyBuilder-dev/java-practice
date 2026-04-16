package com.example.customerbatch.repository;

import com.example.customerbatch.model.MarketingConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MarketingConsentRepository extends JpaRepository<MarketingConsent, UUID> {

    // 만료된 동의 조회
    @Query("SELECT mc FROM MarketingConsent mc WHERE mc.consented = true AND mc.expiresAt < :now")
    List<MarketingConsent> findExpiredConsents(LocalDateTime now);
}
