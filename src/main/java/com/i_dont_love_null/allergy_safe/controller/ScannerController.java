package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.service.PublicAPIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Scanner", description = "바코드 스캔 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scanner")
public class ScannerController {
    private final PublicAPIService publicAPIService;

    @GetMapping("/food")
    public ResponseEntity<IdResponse> scanFood(@RequestParam("barcode") String barcode) {
        return ResponseEntity.status(HttpStatus.OK).body(publicAPIService.getFoodIdFromApiByBarcodeNo(barcode));
    }

    @GetMapping("/medicine")
    public ResponseEntity<IdResponse> scanMedicine(@RequestParam("barcode") String barcode) {
        return ResponseEntity.status(HttpStatus.OK).body(publicAPIService.getMedicineIdFromApiByBarcodeNo(barcode));
    }
}
