package com.i_dont_love_null.allergy_safe.dto;

import com.i_dont_love_null.allergy_safe.model.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Service
public class DiaryPeriodResponse {
    private List<Diary> diaryList;

}
