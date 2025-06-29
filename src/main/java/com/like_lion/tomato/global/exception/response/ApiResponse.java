package com.like_lion.tomato.global.exception.response;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String status,
        T data,
        ErrorDetails details
) {

    public record MessageData(String message) {}

    public record ErrorDetails(
            String code,
            String message,
            Object details
    ) {}

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("success", null, null);
    }

    public ApiResponse(T data) {
        this("success", data, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    // 메시지만 있는 성공 응답 팩토리 메서드
    public static ApiResponse<MessageData> successWithMessage(String message) {
        return new ApiResponse<>("success", new MessageData(message), null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(code, null, new ErrorDetails(code, message, null));
    }

    public static <T> ApiResponse<T> error(String code, String message, Object details) {
        return new ApiResponse<>(code, null, new ErrorDetails(code, message, details));
    }
}