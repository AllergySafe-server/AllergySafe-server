package com.i_dont_love_null.allergy_safe.controller;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("날짜 형식을 지켜주세요.");
    }

    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<String> handleConversionFailedException(ConversionFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("객체 변환에 실패했습니다.");
    }
}
