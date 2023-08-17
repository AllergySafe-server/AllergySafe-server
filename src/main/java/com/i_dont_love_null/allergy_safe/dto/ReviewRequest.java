package com.i_dont_love_null.allergy_safe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewRequest {
    @NotNull(message = "별점을 입력해 주세요.")
    @Min(value = 1, message = "최소 별점은 1점 입니다.")
    @Max(value = 5, message = "최대 별점은 5점 입니다.")
    @Schema
    private Integer star;

    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(min = 5, max = 100, message = "내용은 5자 이상 100자 이하로 입력해 주세요.")
    @Schema
    private String content;
}
