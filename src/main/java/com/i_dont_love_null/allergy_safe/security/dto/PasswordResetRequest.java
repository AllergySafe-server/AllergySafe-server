package com.i_dont_love_null.allergy_safe.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PasswordResetRequest {

    private String token;

    @NotBlank(message = "{new_password_is_empty}")
    @Schema(description = "새로운 비밀번호", example = "newPassword@123")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#^*?])[a-zA-Z0-9~!@#^*?]{8,20}", message = "{password_is_not_valid}")
    private String newPassword;


}
