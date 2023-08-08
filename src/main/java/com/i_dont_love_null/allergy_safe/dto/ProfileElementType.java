package com.i_dont_love_null.allergy_safe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProfileElementType {
    ALLERGY("allergy"),
    MATERIAL("material"),
    INGREDIENT("ingredient");

    private final String value;

    @JsonCreator
    public static ProfileElementType from(String value) {
        for (ProfileElementType elementType : ProfileElementType.values()) {
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
