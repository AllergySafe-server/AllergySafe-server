package com.i_dont_love_null.allergy_safe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DiaryElementType {

    FOOD("food"),
    MEDICINE("medicine"),
    SYMPTOM("symptom");
    private final String value;

    @JsonCreator
    public static DiaryElementType from(String value) {
        for (DiaryElementType diaryElementType : DiaryElementType.values()) {
            if (diaryElementType.getValue().equals(value)) {
                return diaryElementType;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
