package com.johanlund.exceptions;

public class InvalidEventType extends RuntimeException {
    public InvalidEventType(String message) {
        super(message);
    }
}
