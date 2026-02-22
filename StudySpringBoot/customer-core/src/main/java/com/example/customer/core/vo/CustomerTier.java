package com.example.customer.core.vo;

import java.math.BigDecimal;

/**
 * Customer Tier Value Object
 * - 고객 등급 및 승급 규칙 캡슐화
 * - 불변 객체로 도메인 규칙 표현
 */
public enum CustomerTier {
    BRONZE(0L, 500000L),
    SILVER(500000L, 1000000L),
    GOLD(1000000L, 5000000L),
    PLATINUM(5000000L, 10000000L),
    DIAMOND(10000000L, Long.MAX_VALUE);

    private final long minAmount;
    private final long maxAmount;

    CustomerTier(long minAmount, long maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    /**
     * 구매 금액 기준으로 적절한 등급 결정
     * @param purchaseAmount 총 구매 금액
     * @return 해당하는 고객 등급
     */
    public static CustomerTier fromPurchaseAmount(BigDecimal purchaseAmount) {
        if (purchaseAmount == null) {
            return BRONZE;
        }

        long amount = purchaseAmount.longValue();

        for (CustomerTier tier : values()) {
            if (amount >= tier.minAmount && amount < tier.maxAmount) {
                return tier;
            }
        }

        return DIAMOND; // 최대 금액 초과시
    }

    /**
     * 다음 등급으로 승급하기 위한 필요 금액
     */
    public BigDecimal getRequiredAmountForNextTier() {
        if (this == DIAMOND) {
            return BigDecimal.ZERO; // 최고 등급
        }
        return BigDecimal.valueOf(maxAmount);
    }

    public long getMinAmount() {
        return minAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }
}
