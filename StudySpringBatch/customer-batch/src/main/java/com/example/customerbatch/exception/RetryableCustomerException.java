package com.example.customerbatch.exception;

/**
 * Retry 가능한 예외 (테스트용)
 * - 일시적인 오류를 시뮬레이션 (네트워크 오류, DB 락 등)
 * - 재시도하면 성공할 수 있는 경우
 */
public class RetryableCustomerException extends RuntimeException {
    public RetryableCustomerException(String message) {
        super(message);
    }

    public RetryableCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
