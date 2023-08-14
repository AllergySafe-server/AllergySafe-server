package com.i_dont_love_null.allergy_safe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CardElementType {

    FOOD("food"),
    MEDICINE("medicine");
    private final String value;

    @JsonCreator
    public static CardElementType from(String value) {
        for (CardElementType diaryElementType : CardElementType.values()) {
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
