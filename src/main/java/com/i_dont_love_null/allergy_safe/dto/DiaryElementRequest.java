package com.i_dont_love_null.allergy_safe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Service
public class DiaryElementRequest {
    @Schema(description = "추가할 요소의 타입(food, medicine, symptom)", example = "food")
    private DiaryElementType diaryElementType;

    @Schema(description = "추가할 요소의 id(food라면 food의 id)", example = "1")
    private Long id;

    @Schema(description = "추가할 요소를 동작한 시간(food라면 food를 먹은 시간", example = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
}
