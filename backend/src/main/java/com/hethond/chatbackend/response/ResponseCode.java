package com.hethond.chatbackend.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    SUCCESS(HttpStatus.OK),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND),
    ACCESS_DENIED(HttpStatus.FORBIDDEN),
    ACCOUNT_BANNED(HttpStatus.FORBIDDEN),
    ACCOUNT_UNVERIFIED(HttpStatus.FORBIDDEN),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    CONFLICT(HttpStatus.CONFLICT),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    VERIFICATION_FAILED(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    ResponseCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
