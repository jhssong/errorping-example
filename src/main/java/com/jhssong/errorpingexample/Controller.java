package com.jhssong.errorpingexample;

import com.jhssong.errorpingexample.exception.CustomException;
import com.jhssong.errorpingexample.exception.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Controller {
    /**
     * HttpRequestMethodNotSupportedException
     * - GET만 허용된 엔드포인트에 POST 요청 시 발생
     */
    @GetMapping("/method")
    public String methodNotSupported() {
        return "GET only";
    }

    /**
     * IllegalArgumentException
     * - 비즈니스 로직에서 명시적으로 발생
     */
    @GetMapping("/illegal")
    public String illegalArgument(@RequestParam String name) {
        if ("bad".equals(name)) {
            throw new IllegalArgumentException("부적절한 이름입니다.");
        }
        return "Hello, " + name;
    }

    /**
     * MethodArgumentNotValidException
     * - @Valid + @RequestBody 조합에서 발생
     */
    @PostMapping("/validation")
    public String validation(@Valid @RequestBody RequestDTO request) {
        return "ok";
    }

    /**
     * MethodArgumentTypeMismatchException
     * - PathVariable 타입 변환 실패 시 발생
     */
    @GetMapping("/type/{id}")
    public String typeMismatch(@PathVariable Long id) {
        return "ID: " + id;
    }

    /**
     * CustomException
     * - 사용자가 정의한 Exception 테스트 (message X)
     */
    @PostMapping("/custom/no-message")
    public String customNoMessage() {
        throw new CustomException(ErrorCode.TEST_NO_MESSAGE);
    }

    /**
     * CustomException
     * - 사용자가 정의한 Exception 테스트 (message O)
     * - Discord로 알림이 전송됨
     */
    @PostMapping("/custom/with-message")
    public String customWithMessage() {
        throw new CustomException(ErrorCode.TEST_WITH_MESSAGE);
    }
}
