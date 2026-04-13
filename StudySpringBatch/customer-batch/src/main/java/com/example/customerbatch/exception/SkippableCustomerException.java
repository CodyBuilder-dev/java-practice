package com.example.customerbatch.exception;

/**
 * Skip 가능한 예외 (테스트용)
 */
public class SkippableCustomerException extends RuntimeException {
    public SkippableCustomerException(String message) {
        super(message);
    }

    public SkippableCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
