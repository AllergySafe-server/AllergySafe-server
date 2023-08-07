package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.model.Allergy;
import com.i_dont_love_null.allergy_safe.repository.AllergyRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Allergy", description = "알레르기 항원 목록 조회 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/allergy")
public class AllergyController {
    private final AllergyRepository allergyRepository;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Allergy>> getAllergies() {
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyRepository.findAllByOrderById());
    }
}
