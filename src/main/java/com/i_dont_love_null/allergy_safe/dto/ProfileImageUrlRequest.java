package com.i_dont_love_null.allergy_safe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Service
public class ProfileImageUrlRequest {

    @NotBlank(message = "Link 필드는 필수입니다.")
    @Schema(description = "base64String", example = "data:image/png;base64,iVBORw0KGgoAAAANSU...")
    private String base64String;
}
