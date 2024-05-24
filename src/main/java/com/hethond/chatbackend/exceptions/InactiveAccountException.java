package com.hethond.chatbackend.exceptions;

public class InactiveAccountException extends RuntimeException {
    private final String phone;

    public InactiveAccountException(String message) {
        super(message);
        this.phone = null;
    }

    public InactiveAccountException(String message, String phone) {
        super(message);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
