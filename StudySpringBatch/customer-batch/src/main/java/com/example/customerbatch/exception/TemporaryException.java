package com.example.customerbatch.exception;

/**
 * 일시적인 예외 (테스트용)
 * - 첫 번째 시도에서는 실패, 두 번째 시도에서는 성공하는 경우를 시뮬레이션
 */
public class TemporaryException extends RuntimeException {
    public TemporaryException(String message) {
        super(message);
    }

    public TemporaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
