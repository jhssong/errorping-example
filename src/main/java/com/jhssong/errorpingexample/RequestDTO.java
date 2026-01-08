package com.jhssong.errorpingexample;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestDTO {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email
    private String email;
}
