package com.i_dont_love_null.allergy_safe.exceptions;

import com.i_dont_love_null.allergy_safe.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice(basePackageClasses = UserController.class)
public class RegistrationControllerAdvice {

    @ExceptionHandler(RegistrationException.class)
    ResponseEntity<ApiExceptionResponse> handleRegistrationException(RegistrationException exception) {

        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
