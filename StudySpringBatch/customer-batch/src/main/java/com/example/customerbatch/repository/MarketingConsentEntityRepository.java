package com.example.customerbatch.repository;

import com.example.customerbatch.entity.MarketingConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MarketingConsentEntityRepository extends JpaRepository<MarketingConsentEntity, UUID> {

    // 만료된 동의 조회
    @Query("SELECT mc FROM MarketingConsentEntity mc WHERE mc.consented = true AND mc.expiresAt < :now")
    List<MarketingConsentEntity> findExpiredConsents(LocalDateTime now);
}
