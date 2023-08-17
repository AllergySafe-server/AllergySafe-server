package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.FoodRequest;
import com.i_dont_love_null.allergy_safe.dto.FoodResponse;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.service.FoodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Tag(name = "Food", description = "식품 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodController {
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<IdResponse> createFood(@Valid @RequestBody FoodRequest foodRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFood(foodRequest));
    }

    @GetMapping("/{foodId}")
    @ResponseBody
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable Long foodId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.getFoodResponseById(foodId));
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        return ResponseEntity.status(HttpStatus.OK).body(foodService.getAllFoods());
    }
}
