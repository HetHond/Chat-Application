package com.hethond.chatbackend.exceptions;

public class SmsSubmissionException extends RuntimeException {
    public SmsSubmissionException(String message) {
        super(message);
    }
}
