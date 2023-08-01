package com.i_dont_love_null.allergy_safe.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "{email_is_empty}")
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank(message = "{password_is_empty}")
    @Schema(description = "비밀번호", example = "abcd1234@")
    private String password;

}
