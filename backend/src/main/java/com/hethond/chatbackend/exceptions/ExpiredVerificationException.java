package com.hethond.chatbackend.exceptions;

public class ExpiredVerificationException extends RuntimeException {
    public ExpiredVerificationException(String message) {
        super(message);
    }
}
