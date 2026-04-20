package com.example.University.exception;

public class SeatLimitException extends RuntimeException {
    public SeatLimitException(String message) {
        super(message);
    }
}
