package com.hethond.chatbackend;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ApiResponse<T>  {
    public static <T> ApiResponse<T> success(T data) {
        return success("Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ResponseCode.SUCCESS, message, data);
    }

    public static <T> ApiResponse<T> error(@NonNull ResponseCode code,
                                           @NonNull String message) {
        return new ApiResponse<T>(code, message, null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ResponseCode code;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public ApiResponse(@NonNull ResponseCode code,
                       @Nullable String message,
                       @Nullable T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @NonNull
    public ResponseCode getCode() {
        return code;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Nullable
    public T getData() {
        return data;
    }
}
