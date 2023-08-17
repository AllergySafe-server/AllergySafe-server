package com.i_dont_love_null.allergy_safe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Service
public class DiaryElementCreateRequest {
    @Schema(description = "추가할 요소의 타입(food, medicine, symptom)", example = "food")
    private DiaryElementType diaryElementType;

    @Schema(description = "추가할 요소의 id(food라면 food의 id)", example = "1")
    private Long id;

    @Schema(description = "추가할 요소를 동작한 시간(food라면 food를 먹은 시간)", example = "2023-08-10T20:30:45")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @Schema(description = "base64String", example = "data:image/png;base64,iVBORw0KGgoAAAANSU...")
    private String base64String;
}
