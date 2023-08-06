package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.MedicineResponse;
import com.i_dont_love_null.allergy_safe.service.MedicineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Medicine", description = "의약품 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/medicine")
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicineService.getMedicineById(id));
    }
}
