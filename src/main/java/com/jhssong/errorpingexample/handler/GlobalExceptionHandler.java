package com.jhssong.errorpingexample.handler;

import com.jhssong.errorping.ErrorpingService;
import com.jhssong.errorpingexample.exception.ApiResponse;
import com.jhssong.errorpingexample.exception.CustomException;
import com.jhssong.errorpingexample.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Modify according to your project's specific needs.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorpingService errorpingService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("[400] Validation Failed: {}", message);
        errorpingService.sendErrorToDiscord(e, HttpStatus.BAD_REQUEST, request, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.BAD_REQUEST, message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requiredType = (e.getRequiredType() != null) ? e.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("field '%s' expected type '%s'", e.getName(), requiredType);
        log.info("[400] Type Mismatch: {}", message);
        errorpingService.sendErrorToDiscord(e, HttpStatus.BAD_REQUEST, request, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.BAD_REQUEST, message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {
        log.warn("[400] Illegal Argument: {}", e.getMessage());
        errorpingService.sendErrorToDiscord(e, HttpStatus.NOT_FOUND, request, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    protected ResponseEntity<ApiResponse<?>> handleServletRequestBindingException(
            ServletRequestBindingException e, HttpServletRequest request) {
        log.debug("[400] Invalid Access: {}", e.getMessage());
        errorpingService.sendErrorToDiscord(e, HttpStatus.BAD_REQUEST, request, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.debug("[405] Method Not Allowed: {}", e.getMessage());
        errorpingService.sendErrorToDiscord(e, HttpStatus.METHOD_NOT_ALLOWED, request, e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED, e.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handleNoResourceFoundException(
            NoResourceFoundException e, HttpServletRequest request) {
        log.debug("[404] No Resource Found: {}", e.getMessage());
        errorpingService.sendErrorToDiscord(e, HttpStatus.NOT_FOUND, request, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ErrorCode.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e, HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getStatus();
        if (status.is4xxClientError()) {
            log.warn("[{}] CustomException({}): {}", status.value(), errorCode.name(), errorCode.getMessage());
        } else if (status.is5xxServerError()) {
            log.error("[{}] CustomException({}): {}", status.value(), errorCode.name(), errorCode.getMessage(), e);
        }
        errorpingService.sendErrorToDiscord(e, status, request, errorCode.getMessage());
        return ResponseEntity.status(status)
                .body(ApiResponse.error(errorCode));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<?>> handleException(Exception e, HttpServletRequest request) {
        log.error("[500] Internal Server Error", e);
        errorpingService.sendErrorToDiscord(e, HttpStatus.INTERNAL_SERVER_ERROR, request, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
