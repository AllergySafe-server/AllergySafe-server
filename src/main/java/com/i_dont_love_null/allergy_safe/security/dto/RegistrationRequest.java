package com.i_dont_love_null.allergy_safe.security.dto;

import com.i_dont_love_null.allergy_safe.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationRequest {
    @NotBlank(message = "{email_is_empty}")
    @Email(message = "{email_is_not_valid}")
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank(message = "{name_is_empty}")
    @Pattern(regexp = "[가-힣]{2,4}", message = "{name_is_not_valid}")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank(message = "{password_is_empty}")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[~!@#^*?])[a-zA-Z0-9~!@#^*?]{8,20}", message = "{password_is_not_valid}")
    @Schema(description = "비밀번호", example = "abcd1234@")
    private String password;

    public User toEntity() {
        String emailToken = UUID.randomUUID().toString();
        return User.builder()
                .email(email)
                .name(name)
                .password(new BCryptPasswordEncoder().encode(password))
                .emailToken(emailToken)
                .build();
    }
}
