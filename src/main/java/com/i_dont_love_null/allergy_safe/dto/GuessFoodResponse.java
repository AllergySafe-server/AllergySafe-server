package com.i_dont_love_null.allergy_safe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Service
public class GuessFoodResponse {
    private Long profileId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<GuessedFoodData> guessedData;

}
