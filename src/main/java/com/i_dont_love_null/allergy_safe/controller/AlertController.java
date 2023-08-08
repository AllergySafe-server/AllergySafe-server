package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.AlertRequest;
import com.i_dont_love_null.allergy_safe.dto.AlertResponse;
import com.i_dont_love_null.allergy_safe.security.service.UserDetailsServiceImpl;
import com.i_dont_love_null.allergy_safe.service.AlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "Alert", description = "경보 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alert")
public class AlertController {
    private final AlertService alertService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/food")
    public ResponseEntity<AlertResponse> alertFood(@Valid @RequestBody AlertRequest alertRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(alertService.alert(true, userDetailsService.loadCurrentUser(), alertRequest));
    }

    @PostMapping("/medicine")
    public ResponseEntity<AlertResponse> alertMedicine(@Valid @RequestBody AlertRequest alertRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(alertService.alert(false, userDetailsService.loadCurrentUser(), alertRequest));
    }
}
