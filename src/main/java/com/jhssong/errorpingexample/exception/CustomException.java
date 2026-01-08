package com.jhssong.errorpingexample.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
    private HttpStatus status;
    private String code;
    private String title;
    private String message;
    private boolean shouldAlert;

    public CustomException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.title = errorCode.getTitle();
        this.message = errorCode.getMessage();
        this.shouldAlert = errorCode.isShouldAlert();
    }
}
