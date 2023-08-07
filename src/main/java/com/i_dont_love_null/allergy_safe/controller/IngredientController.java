package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.model.Ingredient;
import com.i_dont_love_null.allergy_safe.repository.IngredientRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Ingredient", description = "약품 성분 목록 조회 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredient")
public class IngredientController {
    private final IngredientRepository ingredientRepository;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientRepository.findAllByOrderById());
    }
}
