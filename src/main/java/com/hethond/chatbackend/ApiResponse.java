package com.hethond.chatbackend;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ApiResponse<T>  {
    public static <T> ApiResponse<T> success(@NonNull T data) {
        return new ApiResponse<T>(200, "success", data);
    }

    public static <T> ApiResponse<T> error(@NonNull Integer code,
                                           @NonNull String message) {
        return new ApiResponse<T>(code, message, null);
    }

    private final int code;
    private final String message;
    private final T data;

    public ApiResponse(@NonNull Integer code,
                       @Nullable String message,
                       @Nullable T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @NonNull
    public int getCode() {
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
