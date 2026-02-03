package com.jhssong.errorpingexample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/test";

    @Test
    @DisplayName("HttpRequestMethodNotSupportedException")
    void httpRequestMethodNotSupportedTest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/method"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.title").value("지원되지 않는 요청 메서드입니다."))
                .andExpect(jsonPath("$.message").value("Request method 'POST' is not supported"));
    }

    @Test
    @DisplayName("IllegalArgumentException")
    void illegalArgumentTest() throws Exception {
        mockMvc.perform(get(BASE_URL + "/illegal")
                        .param("name", "bad"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.message").value("부적절한 이름입니다."));
    }

    @Test
    @DisplayName("MethodArgumentNotValidException Empty Email")
    void methodArgumentNotValidTest_EmptyEmail() throws Exception {
        String invalidJson = "{\"email\": \"\"}";

        mockMvc.perform(post(BASE_URL + "/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("유효성 검사에 실패했습니다."))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @Test
    @DisplayName("MethodArgumentNotValidException Invalid Email")
    void methodArgumentNotValidTest_InvalidEmail() throws Exception {
        String invalidJson = "{\"email\": \"not-email\"}";

        mockMvc.perform(post(BASE_URL + "/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("유효성 검사에 실패했습니다."))
                .andExpect(jsonPath("$.message").value("email 형식이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("MethodArgumentTypeMismatchException")
    void methodArgumentTypeMismatchTest() throws Exception {
        mockMvc.perform(get(BASE_URL + "/type/abc"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("인자 타입이 일치하지 않습니다."))
                .andExpect(jsonPath("$.message").value("'id' 파라미터는 Long 타입이어야 합니다."));
    }

    @Test
    @DisplayName("NoResourceFoundException")
    void noResourceFoundTest() throws Exception {
        mockMvc.perform(get(BASE_URL + "/not-exist"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("요청한 리소스를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.message").value("No static resource test/not-exist."));
    }

    @Test
    @DisplayName("CustomException No message")
    void customExceptionNoMessageTest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/custom/no-message"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("테스트 오류입니다."))
                .andExpect(jsonPath("$.message").value(""));
    }

    @Test
    @DisplayName("CustomException With message")
    void customExceptionWithMessageTest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/custom/with-message"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("테스트 오류입니다."))
                .andExpect(jsonPath("$.message").value("테스트 메세지입니다."));
    }
}
