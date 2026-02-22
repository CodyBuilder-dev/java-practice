package com.example.customerbatch.repository;

import com.example.customer.core.enums.CustomerStatus;
import com.example.customerbatch.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, UUID> {

    // 특정 기간 이상 로그인하지 않은 활성 회원 조회
    @Query("SELECT c FROM CustomerEntity c WHERE c.status = :status AND c.lastLoginAt < :threshold")
    List<CustomerEntity> findByStatusAndLastLoginBefore(CustomerStatus status, LocalDateTime threshold);

    // 상태별 회원 조회
    List<CustomerEntity> findByStatus(CustomerStatus status);
}
