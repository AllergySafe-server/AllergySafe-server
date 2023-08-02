package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.security.dto.PasswordChangeRequest;
import com.i_dont_love_null.allergy_safe.security.dto.PasswordChangeResponse;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationRequest;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationResponse;
import com.i_dont_love_null.allergy_safe.security.service.UserDetailsServiceImpl;
import com.i_dont_love_null.allergy_safe.security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "User", description = "사용자 관련 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest) {

        final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    @PutMapping
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        User currentUser = userDetailsService.loadCurrentUser();

        final PasswordChangeResponse passwordChangeResponse = userService.changePassword(currentUser, passwordChangeRequest);

        return ResponseEntity.status(HttpStatus.OK).body(passwordChangeResponse);
    }
}
