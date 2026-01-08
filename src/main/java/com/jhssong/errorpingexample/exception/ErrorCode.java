package com.jhssong.errorpingexample.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TEST_NO_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "999999", "테스트 오류", "", false),
    TEST_WITH_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "999999", "테스트 오류", "테스트 메세지입니다.", true);

    private final HttpStatus status;
    private final String code;
    private final String title;
    private final String message;
    private final boolean shouldAlert;
}
