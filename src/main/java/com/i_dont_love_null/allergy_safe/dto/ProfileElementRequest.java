package com.i_dont_love_null.allergy_safe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Getter
@Setter
@NoArgsConstructor
@Service
public class ProfileElementRequest {
    @Schema(description = "추가할 요소의 타입(allergy, material, ingredient)", example = "allergy")
    private ProfileElementType profileElementType;

    @Schema(description = "추가할 요소의 id(allergy라면 allergy의 id)", example = "1")
    private Long id;
}
