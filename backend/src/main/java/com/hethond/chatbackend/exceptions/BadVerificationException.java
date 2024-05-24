package com.hethond.chatbackend.exceptions;

public class BadVerificationException extends RuntimeException {
    public BadVerificationException(String message) {
        super(message);
    }
}
