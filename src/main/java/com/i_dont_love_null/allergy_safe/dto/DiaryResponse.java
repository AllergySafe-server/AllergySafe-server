package com.i_dont_love_null.allergy_safe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.i_dont_love_null.allergy_safe.model.IngestedFood;
import com.i_dont_love_null.allergy_safe.model.OccuredSymptom;
import com.i_dont_love_null.allergy_safe.model.TakenMedicine;
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
public class DiaryResponse {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long profileId;
    private List<IngestedFood> ingestedFoods;
    private List<TakenMedicine> takenMedicines;
    private List<OccuredSymptom> occuredSymptoms;

}
