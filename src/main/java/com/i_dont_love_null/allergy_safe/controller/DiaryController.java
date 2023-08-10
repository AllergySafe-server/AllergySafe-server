package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.DiaryElementRequest;
import com.i_dont_love_null.allergy_safe.dto.DiaryRequest;
import com.i_dont_love_null.allergy_safe.dto.DiaryResponse;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.service.DiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Tag(name = "Diary", description = "일기장 관련 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @PostMapping("/{profileId}")
    public ResponseEntity<DiaryResponse> createDiary(@PathVariable("profileId") Long profileId, @RequestBody DiaryRequest diaryRequest) {
        final DiaryResponse diaryResponse = diaryService.createDiary(profileId, diaryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(diaryResponse);
    }

    @PostMapping("/element/{diaryId}")
    public ResponseEntity<IdResponse> addDiaryElement(@PathVariable("diaryId") Long diaryId, @RequestBody DiaryElementRequest diaryElementRequest) {
        final IdResponse idResponse = diaryService.addDiaryElement(diaryId, diaryElementRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

    @DeleteMapping("/element/{diaryId}")
    public ResponseEntity<IdResponse> deleteDiaryElement(@PathVariable("diaryId") Long diaryId, @Valid @RequestBody DiaryElementRequest diaryElementRequest) {
        final IdResponse idResponse = diaryService.deleteDiaryElement(diaryId, diaryElementRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(idResponse);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<DiaryResponse> getDiaryList(@PathVariable("profileId") Long profileId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        final DiaryResponse diaryResponse = diaryService.getDiaryList(profileId, date);
        return ResponseEntity.status(HttpStatus.OK).body(diaryResponse);
    }

    @ExceptionHandler({DateTimeParseException.class, ConversionFailedException.class})
    public ResponseEntity<String> handleDateTimeParseException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("날짜 형식을 'yyyy-mm-dd'으로 지켜주세요");
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<String> deleteDiary(@PathVariable("diaryId") Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok("해당 다이어리가 삭제 되었습니다.");
    }


}
