package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.model.Material;
import com.i_dont_love_null.allergy_safe.repository.MaterialRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Material", description = "식품 원재료 목록 조회 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/material")
public class MaterialController {
    private final MaterialRepository materialRepository;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Material>> getMaterials() {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialRepository.findAllByOrderById());
    }
}
