package org.example.springv4.core.error.ex;

// 인증관련
public class ExceptionApi403 extends RuntimeException {
    public ExceptionApi403(String message) {
        super(message);
    }
}
