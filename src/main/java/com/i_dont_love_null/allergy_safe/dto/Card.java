package com.i_dont_love_null.allergy_safe.dto;

import com.i_dont_love_null.allergy_safe.model.Symptom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Service
public class Card {
    private CardElementType type;
    private Long elementId;
    private String name;
    private LocalDateTime dateTime;
    private List<Symptom> symptoms;
}
