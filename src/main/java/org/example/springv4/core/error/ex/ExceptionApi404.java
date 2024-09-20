package org.example.springv4.core.error.ex;

// 인증관련
public class ExceptionApi404 extends RuntimeException {
    public ExceptionApi404(String message) {
        super(message);
    }
}
