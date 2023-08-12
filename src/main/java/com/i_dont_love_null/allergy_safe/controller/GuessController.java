package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.GuessFoodResponse;
import com.i_dont_love_null.allergy_safe.service.GuessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Tag(name = "Guess", description = "알레르기 항원 추론 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guess")
public class GuessController {
    private final GuessService guessService;

    @GetMapping("/{profileId}")
    @ResponseBody
    public ResponseEntity<GuessFoodResponse> getFoodById(@PathVariable("profileId") Long profileId, @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(guessService.guessing(profileId, startDate, endDate));
    }

}
