package com.hethond.chatbackend;

import com.hethond.chatbackend.exceptions.InactiveAccountException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e, HttpServletRequest request) {
        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An internal error has occurred. Please contact support or try again later.");
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<ApiResponse<Object>> handleInactiveAccountException(InactiveAccountException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiResponse<Object> response = ApiResponse.error(
                status.value(),
                "Your account is inactive.",
                "/verify/" + e.getPhone()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException e, HttpServletRequest request) {
        ApiResponse<Object> response = ApiResponse.error(
                e.getCode(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getCode()));
    }
}
