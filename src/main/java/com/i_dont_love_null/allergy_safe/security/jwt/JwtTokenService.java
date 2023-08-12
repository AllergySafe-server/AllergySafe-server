package com.i_dont_love_null.allergy_safe.security.jwt;

import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.security.dto.LoginRequest;
import com.i_dont_love_null.allergy_safe.security.dto.LoginResponse;
import com.i_dont_love_null.allergy_safe.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {

        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        final User user = userRepository.findByEmail(email);
        final String token = jwtTokenManager.generateToken(user);

        if (!user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일인증이 완료되지 않았거나 탈퇴된 사용자입니다");
        }

        return new LoginResponse(token);
    }
}
