package com.jhssong.errorpingexample.exception.resolver;

import com.jhssong.errorping.exception.ErrorResponse;
import com.jhssong.errorping.exception.resolver.ExceptionResolver;
import com.jhssong.errorpingexample.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomExceptionResolver implements ExceptionResolver {

    @Override
    public boolean support(Throwable ex) {
        return ex instanceof CustomException;
    }

    @Override
    public ErrorResponse resolve(Throwable ex, HttpServletRequest httpServletRequest) {
        CustomException e = (CustomException) ex;
        return ErrorResponse.builder()
                .status(e.getStatus())
                .code(e.getCode())
                .title(e.getTitle())
                .message(e.getMessage())
                .build();
    }

    @Override
    public LogLevel logLevel() {
        return LogLevel.ERROR;
    }

    @Override
    public String logMessage(ErrorResponse er, HttpServletRequest request) {
        return String.format("[CustomException] status=%s method=%s uri=%s message=%s",
                er.getStatus(),
                request.getMethod(),
                request.getRequestURI(),
                er.getMessage());
    }

    @Override
    public boolean shouldAlert(Throwable ex) {
        CustomException e = (CustomException) ex;
        if (e.isShouldAlert()) {
            log.error("Exception Detail: ", e);
        }
        return e.isShouldAlert();
    }
}
