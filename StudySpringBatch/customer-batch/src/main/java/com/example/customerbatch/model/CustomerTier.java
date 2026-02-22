package com.example.customerbatch.model;

/**
 * 고객 등급
 */
public enum CustomerTier {
    BRONZE,      // 브론즈: 0 ~ 50만원
    SILVER,      // 실버: 50만원 ~ 100만원
    GOLD,        // 골드: 100만원 ~ 500만원
    PLATINUM,    // 플래티넘: 500만원 ~ 1000만원
    DIAMOND      // 다이아몬드: 1000만원 이상
}
