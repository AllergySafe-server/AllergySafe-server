package com.i_dont_love_null.allergy_safe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Service
public class GuessedData {
    private GuessedType type;
    private Long elementId;
    private String name;
    private Integer totalCount;
    private Integer totalSymptomOccuredCount;

    private Integer percentage;
    private List<Card> cards;

}
