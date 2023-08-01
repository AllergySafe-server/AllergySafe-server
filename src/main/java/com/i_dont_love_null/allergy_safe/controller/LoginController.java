package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.security.dto.LoginRequest;
import com.i_dont_love_null.allergy_safe.security.dto.LoginResponse;
import com.i_dont_love_null.allergy_safe.security.jwt.JwtTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "Login", description = "로그인 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/login")
public class LoginController {

	private final JwtTokenService jwtTokenService;

	@PostMapping
	public ResponseEntity<LoginResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest) {

		final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);

		return ResponseEntity.ok(loginResponse);
	}

}
