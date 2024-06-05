package com.hethond.chatbackend.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    public static ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND.value(), message);
    }

    private final int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
