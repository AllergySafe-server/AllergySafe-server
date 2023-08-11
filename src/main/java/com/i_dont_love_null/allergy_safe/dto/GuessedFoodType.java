package com.i_dont_love_null.allergy_safe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GuessedFoodType {
    ALLERGY("allergy"),
    MATERIAL("material");

    private final String value;

    @JsonCreator
    public static GuessedFoodType from(String value) {
        for (GuessedFoodType elementType : GuessedFoodType.values()) {
            if (elementType.getValue().equals(value)) {
                return elementType;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
