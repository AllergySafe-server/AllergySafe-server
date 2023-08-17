package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.dto.ReviewRequest;
import com.i_dont_love_null.allergy_safe.dto.ReviewResponse;
import com.i_dont_love_null.allergy_safe.security.service.UserDetailsServiceImpl;
import com.i_dont_love_null.allergy_safe.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "Review", description = "리뷰 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping
    public ResponseEntity<IdResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(userDetailsService.loadCurrentUser(), reviewRequest));
    }

    @GetMapping
    public ResponseEntity<ReviewResponse> getReview() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(userDetailsService.loadCurrentUser()));
    }
}
