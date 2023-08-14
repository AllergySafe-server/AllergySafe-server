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
public class DiaryElementDeleteRequest {
    @Schema(description = "추가할 요소의 타입(food, medicine, symptom)", example = "food")
    private DiaryElementType diaryElementType;

    @Schema(description = "추가할 요소의 id(food라면 food의 id)", example = "1")
    private Long id;
}
