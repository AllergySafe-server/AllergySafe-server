package com.i_dont_love_null.allergy_safe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Service
public class DiaryRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;


}
