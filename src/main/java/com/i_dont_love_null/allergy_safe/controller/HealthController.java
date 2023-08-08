package com.i_dont_love_null.allergy_safe.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "Health Check API")
@RequiredArgsConstructor
@RestController
public class HealthController {
    @GetMapping("/api/health")
    public ResponseEntity<String> sayHello() {

        return ResponseEntity.ok("Good!");
    }
}
