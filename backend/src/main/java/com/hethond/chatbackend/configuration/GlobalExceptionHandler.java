package com.hethond.chatbackend.configuration;

import com.hethond.chatbackend.exceptions.ApiException;
import com.hethond.chatbackend.response.ApiResponse;
import com.hethond.chatbackend.response.ResponseCode;
import com.hethond.chatbackend.exceptions.InactiveAccountException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e, HttpServletRequest request) {
        ApiResponse<Object> response = ApiResponse.error(
                ResponseCode.INTERNAL_SERVER_ERROR,
                "An internal error has occurred. Please contact support or try again later.");
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ResponseEntity<ApiResponse<Object>> handleInactiveAccountException(InactiveAccountException e, HttpServletRequest request) {
        ApiResponse<Object> response = ApiResponse.error(
                ResponseCode.ACCOUNT_INACTIVE,
                "Your account is inactive."
        );
        return new ResponseEntity<>(response, response.getCode().getHttpStatus());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException e, HttpServletRequest request) {
        ApiResponse<Object> response = ApiResponse.error(
                ResponseCode.SUCCESS,
                e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getCode()));
    }
}
