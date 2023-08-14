package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.model.Symptom;
import com.i_dont_love_null.allergy_safe.repository.SymptomRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Symptom", description = "알레르기 증상 목록 조회 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/symptom")
public class SymptomController {

    private final SymptomRepository symptomRepository;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Symptom>> getSymptoms() {
        return ResponseEntity.status(HttpStatus.CREATED).body(symptomRepository.findAllByOrderById());
    }
}
