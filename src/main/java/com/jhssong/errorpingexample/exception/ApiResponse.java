package com.jhssong.errorpingexample.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        ResultType result,
        T data,
        String title,
        String message
) {
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .result(ResultType.SUCCESS)
                .data(data)
                .build();
    }

    public static ApiResponse<?> success() {
        return ApiResponse.builder()
                .result(ResultType.SUCCESS)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .result(ResultType.SUCCESS)
                .data(data)
                .message(message)
                .build();
    }

    public static ApiResponse<?> error(ErrorCode errorCode) {
        return ApiResponse.builder()
                .result(ResultType.FAIL)
                .title(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static ApiResponse<?> error(ErrorCode errorCode, String message) {
        return ApiResponse.builder()
                .result(ResultType.FAIL)
                .title(errorCode.name())
                .message(message)
                .build();
    }
}
