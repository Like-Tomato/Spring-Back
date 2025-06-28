package com.like_lion.tomato.global.exception.response;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String status,
        T data,
        ErrorDetails details
) {
    public record ErrorDetails(
            String code,
            String message,
            Object details
    ) {}

    public ApiResponse(T data) {
        this("success", data, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(code, null, new ErrorDetails(code, message, null));
    }

    public static <T> ApiResponse<T> error(String code, String message, Object details) {
        return new ApiResponse<>(code, null, new ErrorDetails(code, message, details));
    }
}