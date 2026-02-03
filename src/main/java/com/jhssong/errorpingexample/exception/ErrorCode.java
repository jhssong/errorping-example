package com.jhssong.errorpingexample.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TEST_NO_MESSAGE("-1","테스트 오류입니다.", "", HttpStatus.INTERNAL_SERVER_ERROR,  LogLevel.ERROR, false),
    TEST_WITH_MESSAGE("-1","테스트 오류입니다.", "테스트 메세지입니다.", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR,  true);

    private final String code;
    private final String title;
    private final String message;
    private final HttpStatus status;
    private final LogLevel logLevel;
    private final boolean shouldAlert;
}
