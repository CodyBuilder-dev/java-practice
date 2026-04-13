package com.example.customerbatch.mapper;

import com.example.customerbatch.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * MyBatis Mapper for Customer Batch Operations
 */
@Mapper
public interface CustomerBatchMapper {

    /**
     * Find dormant customers (last login > 90 days ago)
     */
    List<CustomerEntity> findDormantCustomers(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Find customers by status
     */
    List<CustomerEntity> findByStatus(@Param("status") String status);

    /**
     * Find customers eligible for VIP tier (high purchase amount in last 6 months)
     */
    List<CustomerEntity> findVipEligibleCustomers(
        @Param("minTotalAmount") double minTotalAmount,
        @Param("startDate") LocalDateTime startDate
    );

    /**
     * Update customer status
     */
    int updateCustomerStatus(
        @Param("customerId") UUID customerId,
        @Param("status") String status,
        @Param("updatedAt") LocalDateTime updatedAt
    );

    /**
     * Update customer tier
     */
    int updateCustomerTier(
        @Param("customerId") UUID customerId,
        @Param("tier") String tier,
        @Param("updatedAt") LocalDateTime updatedAt
    );

    /**
     * Get total order amount for customer in date range
     */
    Double getTotalOrderAmount(
        @Param("customerId") UUID customerId,
        @Param("startDate") LocalDateTime startDate
    );

    /**
     * Find customers with pagination (for chunk processing)
     */
    List<CustomerEntity> findCustomersWithPagination(
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    /**
     * Count total customers
     */
    long countCustomers();
}
