package com.example.customerbatch.exception;

/**
 * Skip 불가능한 예외 (테스트용)
 */
public class NonSkippableCustomerException extends RuntimeException {
    public NonSkippableCustomerException(String message) {
        super(message);
    }

    public NonSkippableCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
