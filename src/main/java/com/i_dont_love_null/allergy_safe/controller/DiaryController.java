package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.*;
import com.i_dont_love_null.allergy_safe.service.DiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Tag(name = "Diary", description = "일기장 관련 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @PostMapping("/{profileId}")
    public ResponseEntity<IdResponse> createDiary(@PathVariable("profileId") Long profileId, @RequestBody DiaryRequest diaryRequest) {
        final IdResponse idResponse = diaryService.createDiary(profileId, diaryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(idResponse);
    }

    @PostMapping("/element/{diaryId}")
    public ResponseEntity<IdResponse> addDiaryElement(@PathVariable("diaryId") Long diaryId, @RequestBody DiaryElementCreateRequest diaryElementCreateRequest) {
        final IdResponse idResponse = diaryService.addDiaryElement(diaryId, diaryElementCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

    @DeleteMapping("/element/{diaryId}")
    public ResponseEntity<IdResponse> deleteDiaryElement(@PathVariable("diaryId") Long diaryId, @Valid @RequestBody DiaryElementDeleteRequest diaryElementDeleteRequest) {
        final IdResponse idResponse = diaryService.deleteDiaryElement(diaryId, diaryElementDeleteRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<DiaryResponse> getDiaryList(@PathVariable("profileId") Long profileId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        final DiaryResponse diaryResponse = diaryService.getDiaryList(profileId, date);
        return ResponseEntity.status(HttpStatus.OK).body(diaryResponse);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<IdResponse> deleteDiary(@PathVariable("diaryId") Long diaryId) {
        final IdResponse idResponse = diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok(idResponse);
    }

    @GetMapping("/period")
    public ResponseEntity<DiaryPeriodResponse> getDiaryPeriod(@RequestParam("profileId") Long profileId,
                                                              @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                              @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        final DiaryPeriodResponse diaryPeriodResponse = diaryService.getDiaryPeriod(profileId, startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(diaryPeriodResponse);
    }


}
